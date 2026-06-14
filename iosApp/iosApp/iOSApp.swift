import SwiftUI

@main
struct iOSApp: App {
    init() {
        IosLocalRecipeLlmBootstrap.register()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
