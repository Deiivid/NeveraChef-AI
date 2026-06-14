import UIKit
import SwiftUI
import Shared
import Speech
import AVFoundation

final class IosSpeechToTextController: NSObject, SFSpeechRecognizerDelegate {
    private let recognizer = SFSpeechRecognizer(locale: Locale(identifier: "es_ES"))
    private let audioEngine = AVAudioEngine()
    private var request: SFSpeechAudioBufferRecognitionRequest?
    private var task: SFSpeechRecognitionTask?
    private var completion: ((String) -> Void)?
    private var latestText = ""

    func recognize(prompt: String, completion: @escaping (String) -> Void) {
        requestAuthorization { [weak self] granted in
            guard granted, let self else {
                completion("")
                return
            }
            self.startRecording(completion: completion)
        }
    }

    private func requestAuthorization(completion: @escaping (Bool) -> Void) {
        SFSpeechRecognizer.requestAuthorization { speechStatus in
            AVAudioSession.sharedInstance().requestRecordPermission { microphoneGranted in
                DispatchQueue.main.async {
                    completion(speechStatus == .authorized && microphoneGranted)
                }
            }
        }
    }

    private func startRecording(completion: @escaping (String) -> Void) {
        stopRecording(sendResult: false)
        latestText = ""
        self.completion = completion

        guard let recognizer, recognizer.isAvailable else {
            finish(with: "")
            return
        }

        if #available(iOS 13.0, *), !recognizer.supportsOnDeviceRecognition {
            finish(with: "")
            return
        }

        let audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession.setCategory(.record, mode: .measurement, options: .duckOthers)
            try audioSession.setActive(true, options: .notifyOthersOnDeactivation)

            let request = SFSpeechAudioBufferRecognitionRequest()
            request.shouldReportPartialResults = true
            if #available(iOS 13.0, *) {
                request.requiresOnDeviceRecognition = true
            }
            self.request = request

            let inputNode = audioEngine.inputNode
            let format = inputNode.outputFormat(forBus: 0)
            inputNode.removeTap(onBus: 0)
            inputNode.installTap(onBus: 0, bufferSize: 1024, format: format) { [weak request] buffer, _ in
                request?.append(buffer)
            }

            audioEngine.prepare()
            try audioEngine.start()

            task = recognizer.recognitionTask(with: request) { [weak self] result, error in
                guard let self else { return }
                if let result {
                    self.latestText = result.bestTranscription.formattedString
                    if result.isFinal {
                        self.finish(with: self.latestText)
                    }
                }
                if error != nil {
                    self.finish(with: self.latestText)
                }
            }
        } catch {
            finish(with: "")
        }
    }

    private func finish(with text: String) {
        stopRecording(sendResult: false)
        let currentCompletion = completion
        completion = nil
        currentCompletion?(text)
    }

    private func stopRecording(sendResult: Bool) {
        if audioEngine.isRunning {
            audioEngine.stop()
            audioEngine.inputNode.removeTap(onBus: 0)
        }
        request?.endAudio()
        task?.cancel()
        request = nil
        task = nil
        if sendResult {
            let currentCompletion = completion
            completion = nil
            currentCompletion?(latestText)
        }
        try? AVAudioSession.sharedInstance().setActive(false, options: .notifyOthersOnDeactivation)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    private let speechToText = IosSpeechToTextController()

    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController(onRequestSpeechToText: { prompt, onResult in
            speechToText.recognize(prompt: prompt) { text in
                onResult(text)
            }
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
