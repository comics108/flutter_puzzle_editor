import React, { useRef, forwardRef, useImperativeHandle } from 'react';
import {
  requireNativeComponent,
  UIManager,
  Platform,
  findNodeHandle,
  type ViewStyle,
  type NativeSyntheticEvent,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-comics-viewer' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type ScrollChangedEvent = NativeSyntheticEvent<{ position: number }>;
type ErrorEvent = NativeSyntheticEvent<{ error: string }>;

export interface ComicsViewerProps {
  filePath: string;
  onLoaded?: () => void;
  onScrollChanged?: (position: number) => void;
  onError?: (error: string) => void;
  languageIndex?: number;
  soundEnabled?: boolean;
  showPreview?: boolean;
  style?: ViewStyle;
}

export interface ComicsViewerRef {
  loadComics(filePath: string): Promise<void>;
  play(): void;
  pause(): void;
  setScrollPosition(position: number): void;
  getScrollPosition(): number;
  togglePreview(show: boolean): void;
  toggleSounds(enabled: boolean): void;
  dispose(): void;

  readonly isPlaying: boolean;
  readonly duration: number;
  readonly currentPosition: number;
}

interface NativeComicsViewerProps {
  filePath?: string;
  languageIndex?: number;
  soundEnabled?: boolean;
  style?: ViewStyle;
  onScrollChanged?: (event: ScrollChangedEvent) => void;
  onLoaded?: () => void;
  onError?: (event: ErrorEvent) => void;
}

const ComponentName = 'ComicsViewerView';

const NativeComicsViewerView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<NativeComicsViewerProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

const ComicsViewer = forwardRef<ComicsViewerRef, ComicsViewerProps>(
  (
    {
      filePath,
      onLoaded,
      onScrollChanged,
      onError,
      languageIndex = 0,
      soundEnabled = true,
      showPreview = false,
      style,
    },
    ref
  ) => {
    const nativeRef = useRef(null);

    useImperativeHandle(ref, () => ({
      loadComics: async (path: string) => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(
            node,
            'loadComics' as any,
            [path]
          );
        }
      },
      play: () => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(node, 'play' as any, []);
        }
      },
      pause: () => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(node, 'pause' as any, []);
        }
      },
      setScrollPosition: (position: number) => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(
            node,
            'setScrollPosition' as any,
            [position]
          );
        }
      },
      getScrollPosition: () => {
        // This would need to be implemented differently in React Native
        // since commands are one-way. Would need to use a state variable
        // updated via onScrollChanged
        return 0;
      },
      togglePreview: (show: boolean) => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(
            node,
            'togglePreview' as any,
            [show]
          );
        }
      },
      toggleSounds: (enabled: boolean) => {
        const node = findNodeHandle(nativeRef.current);
        if (node) {
          UIManager.dispatchViewManagerCommand(
            node,
            'toggleSounds' as any,
            [enabled]
          );
        }
      },
      dispose: () => {
        // Cleanup handled by native side
      },
      get isPlaying(): boolean {
        // Would need to be tracked via state
        return false;
      },
      get duration(): number {
        // Would need to be tracked via state
        return 0;
      },
      get currentPosition(): number {
        // Would need to be tracked via state
        return 0;
      },
    }));

    const handleScrollChanged = (event: ScrollChangedEvent) => {
      if (onScrollChanged) {
        onScrollChanged(event.nativeEvent.position);
      }
    };

    const handleError = (event: ErrorEvent) => {
      if (onError) {
        onError(event.nativeEvent.error);
      }
    };

    return (
      <NativeComicsViewerView
        ref={nativeRef}
        filePath={filePath}
        languageIndex={languageIndex}
        soundEnabled={soundEnabled}
        onScrollChanged={handleScrollChanged}
        onLoaded={onLoaded}
        onError={handleError}
        style={style}
      />
    );
  }
);

ComicsViewer.displayName = 'ComicsViewer';

export default ComicsViewer;
export { ComicsViewer };
