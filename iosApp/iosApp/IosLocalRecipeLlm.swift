import Dispatch
import Foundation
import LiteRTLM
import Shared

private let localRecipeLlmModelName = "Gemma 3 270M IT"
private let localRecipeLlmFileName = "gemma-3-270m-it"
private let localRecipeLlmExtension = "litertlm"

enum IosLocalRecipeLlmBootstrap {
    static func register() {
        let engine = IosLocalRecipeLlmEngine()
        IosLocalLlmBridgeKt.configureIosLocalLlmEngine(
            modelName: engine.isReady ? engine.modelName : nil,
            isReady: engine.isReady,
            generate: { prompt in engine.generate(prompt: prompt) }
        )
    }
}

private final class IosLocalRecipeLlmEngine {
    let modelName = localRecipeLlmModelName

    var isReady: Bool {
        modelPath != nil
    }

    func generate(prompt: String) -> String? {
        guard let path = modelPath else { return nil }

        let semaphore = DispatchSemaphore(value: 0)
        var output: String?

        Task.detached {
            do {
                let config = try EngineConfig(
                    modelPath: path,
                    backend: .gpu,
                    maxNumTokens: 512,
                    cacheDir: NSTemporaryDirectory()
                )
                let engine = Engine(engineConfig: config)
                try await engine.initialize()
                let conversation = try await engine.createConversation()
                let response = try await conversation.sendMessage(Message(prompt))
                output = response.toString
            } catch {
                output = nil
            }
            semaphore.signal()
        }

        _ = semaphore.wait(timeout: .now() + 30)
        return output?.isEmpty == false ? output : nil
    }

    private var modelPath: String? {
        if let bundledInModels = Bundle.main.path(
            forResource: localRecipeLlmFileName,
            ofType: localRecipeLlmExtension,
            inDirectory: "Models"
        ) {
            return bundledInModels
        }

        if let bundled = Bundle.main.path(
            forResource: localRecipeLlmFileName,
            ofType: localRecipeLlmExtension
        ) {
            return bundled
        }

        let documents = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first
        return documents?
            .appendingPathComponent("ai_models")
            .appendingPathComponent("\(localRecipeLlmFileName).\(localRecipeLlmExtension)")
            .path
            .flatMap { FileManager.default.fileExists(atPath: $0) ? $0 : nil }
    }
}
