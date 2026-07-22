import Foundation
import UIKit
import ComicsViewer

@objc(ComicsViewerViewManager)
class ComicsViewerViewManager: RCTViewManager {

    override func view() -> UIView! {
        return ComicsViewerView()
    }

    override static func requiresMainQueueSetup() -> Bool {
        return true
    }

    // Export commands
    @objc func loadComics(_ node: NSNumber, filePath: String) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.loadComics(filePath: filePath)
            }
        }
    }

    @objc func play(_ node: NSNumber) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.play()
            }
        }
    }

    @objc func pause(_ node: NSNumber) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.pause()
            }
        }
    }

    @objc func setScrollPosition(_ node: NSNumber, position: Double) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.setScrollPosition(position)
            }
        }
    }

    @objc func togglePreview(_ node: NSNumber, show: Bool) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.togglePreview(show)
            }
        }
    }

    @objc func toggleSounds(_ node: NSNumber, enabled: Bool) {
        DispatchQueue.main.async {
            if let component = self.bridge.uiManager.view(forReactTag: node) as? ComicsViewerView {
                component.toggleSounds(enabled)
            }
        }
    }
}

// MARK: - ComicsViewerView

class ComicsViewerView: UIView {
    private let imageScrollView: ImageScrollView
    private var controller: ComicsViewerController!
    private var filePath: String?

    @objc var onScrollChanged: RCTDirectEventBlock?
    @objc var onLoaded: RCTDirectEventBlock?
    @objc var onError: RCTDirectEventBlock?

    override init(frame: CGRect) {
        imageScrollView = ImageScrollView()
        imageScrollView.isComics = true

        super.init(frame: frame)

        imageScrollView.translatesAutoresizingMaskIntoConstraints = false
        addSubview(imageScrollView)

        NSLayoutConstraint.activate([
            imageScrollView.topAnchor.constraint(equalTo: topAnchor),
            imageScrollView.leadingAnchor.constraint(equalTo: leadingAnchor),
            imageScrollView.trailingAnchor.constraint(equalTo: trailingAnchor),
            imageScrollView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])

        controller = ComicsViewerController(scrollView: imageScrollView)

        // Setup scroll listener
        controller.onScrollChanged = { [weak self] position in
            self?.onScrollChanged?(["position": Double(position)])
        }
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // Props
    @objc func setFilePath(_ path: String?) {
        if let path = path, path != filePath {
            filePath = path
            loadComics(filePath: path)
        }
    }

    @objc func setLanguageIndex(_ index: Int) {
        controller.setLanguage(index)
    }

    @objc func setSoundEnabled(_ enabled: Bool) {
        controller.toggleSounds(enabled)
    }

    // Methods
    func loadComics(filePath: String) {
        controller.loadComics(filePath: filePath) { [weak self] result in
            switch result {
            case .success:
                self?.onLoaded?([:])
            case .failure(let error):
                self?.onError?(["error": error.localizedDescription])
            }
        }
    }

    func play() {
        controller.play()
    }

    func pause() {
        controller.pause()
    }

    func setScrollPosition(_ position: Double) {
        controller.setScrollPosition(CGFloat(position))
    }

    func togglePreview(_ show: Bool) {
        controller.togglePreview(show)
    }

    func toggleSounds(_ enabled: Bool) {
        controller.toggleSounds(enabled)
    }

    deinit {
        controller.dispose()
    }
}
