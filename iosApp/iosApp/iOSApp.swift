import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinHelper.shared.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
