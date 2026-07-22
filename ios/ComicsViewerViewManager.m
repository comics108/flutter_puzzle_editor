#import <React/RCTViewManager.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ComicsViewerViewManager, RCTViewManager)

// Props
RCT_EXPORT_VIEW_PROPERTY(filePath, NSString)
RCT_EXPORT_VIEW_PROPERTY(languageIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(soundEnabled, BOOL)

// Events
RCT_EXPORT_VIEW_PROPERTY(onScrollChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onLoaded, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onError, RCTDirectEventBlock)

// Commands
RCT_EXTERN_METHOD(loadComics:(nonnull NSNumber *)node filePath:(nonnull NSString *)filePath)
RCT_EXTERN_METHOD(play:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(pause:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(setScrollPosition:(nonnull NSNumber *)node position:(double)position)
RCT_EXTERN_METHOD(togglePreview:(nonnull NSNumber *)node show:(BOOL)show)
RCT_EXTERN_METHOD(toggleSounds:(nonnull NSNumber *)node enabled:(BOOL)enabled)

@end
