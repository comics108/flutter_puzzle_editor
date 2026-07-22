# React Native Comics Viewer

A React Native module for rendering interactive comics and puzzles with animations and sound.

## Features

- **Comics Rendering**: Display animated comics with automatic scroll and sound synchronization
- **Puzzle Support**: Render interactive puzzle grids with piece navigation
- **Sound Playback**: Position-synchronized audio playback
- **Cross-Platform**: Works on Android and iOS
- **Simple API**: Just 5 main methods for comics playback control
- **TypeScript Support**: Full TypeScript definitions included

## Installation

```bash
npm install react-native-comics-viewer
# or
yarn add react-native-comics-viewer
```

For iOS, install pods:

```bash
cd ios && pod install
```

## Quick Start

```tsx
import React, { useRef } from 'react';
import ComicsViewer, { ComicsViewerRef } from 'react-native-comics-viewer';

function ComicsScreen() {
  const comicsRef = useRef<ComicsViewerRef>(null);

  return (
    <ComicsViewer
      ref={comicsRef}
      filePath="/path/to/episode.comics"
      onLoaded={() => {
        console.log('Comics loaded!');
        comicsRef.current?.play();
      }}
      onScrollChanged={(position) => {
        console.log('Scroll position:', position);
      }}
      onError={(error) => {
        console.error('Error:', error);
      }}
    />
  );
}
```

## Comics API

### ComicsViewer Component

Main component for displaying comics.

```tsx
<ComicsViewer
  ref={comicsRef}
  filePath={string}              // Path to .comics file (required)
  onLoaded={() => void}          // Called when comics loaded
  onScrollChanged={(position: number) => void}  // Scroll position changed
  onError={(error: string) => void}  // Error occurred
  languageIndex={number}         // Language selection (default: 0)
  soundEnabled={boolean}         // Enable/disable sounds (default: true)
  showPreview={boolean}          // Show/hide preview layers (default: false)
/>
```

### ComicsViewerRef

Reference interface for comics control.

#### Methods

**1. Load and Display**

```typescript
// Load .comics file
await comicsRef.current?.loadComics('/path/to/file.comics');
```

**2. Playback Control**

```typescript
// Start auto-scroll playback
comicsRef.current?.play();

// Pause playback
comicsRef.current?.pause();
```

**3. Navigation**

```typescript
// Set scroll position (0.0 to duration)
comicsRef.current?.setScrollPosition(500.0);

// Get current scroll position
const position = comicsRef.current?.getScrollPosition();
```

**4. Preview & Sound**

```typescript
// Toggle preview layers visibility
comicsRef.current?.togglePreview(true);   // Show preview
comicsRef.current?.togglePreview(false);  // Hide preview

// Toggle sound playback
comicsRef.current?.toggleSounds(true);    // Enable sounds
comicsRef.current?.toggleSounds(false);   // Disable sounds
```

**5. Cleanup**

```typescript
// Release resources (call on unmount)
comicsRef.current?.dispose();
```

#### Properties (Read-Only)

```typescript
// Check if currently playing
const playing = comicsRef.current?.isPlaying;

// Get total scrollable height
const totalHeight = comicsRef.current?.duration;

// Get current position
const currentPos = comicsRef.current?.currentPosition;
```

## Puzzle API

### PuzzleViewer Component

Component for displaying interactive puzzles.

```tsx
<PuzzleViewer
  ref={puzzleRef}
  filePath={string}              // Path to .puzzle file (required)
  onLoaded={() => void}
  onPieceSelected={(index: number) => void}  // Piece index selected
  onError={(error: string) => void}
  soundEnabled={boolean}
  showPreview={boolean}
/>
```

### PuzzleViewerRef

Reference interface for puzzle control.

```typescript
// Load puzzle file
await puzzleRef.current?.loadPuzzle('/path/to/puzzle.puzzle');

// Navigate to piece by index
puzzleRef.current?.selectPiece(5);

// Get current piece
const currentPiece = puzzleRef.current?.currentPieceIndex;

// Get total pieces count
const totalPieces = puzzleRef.current?.totalPieces;

// Control playback for current piece
puzzleRef.current?.play();
puzzleRef.current?.pause();

// Toggle preview/sounds for all pieces
puzzleRef.current?.togglePreview(true);
puzzleRef.current?.toggleSounds(false);

// Cleanup
puzzleRef.current?.dispose();
```

## File Format

### .comics File

A .comics file is a ZIP archive containing:

- `data.json` - Comics structure and metadata
- `layers/` - Layer images (PNG)
- `sounds/` - Audio files (MP3)

**Example usage:**

```typescript
// From app bundle (iOS)
const filePath = 'file://' + RNFS.MainBundlePath + '/episode1.comics';

// From documents directory
const filePath = RNFS.DocumentDirectoryPath + '/episode1.comics';

// From downloaded file (Android)
const filePath = '/data/user/0/com.app/files/episode1.comics';
```

### .puzzle File

A .puzzle file is a ZIP archive containing:

- Puzzle metadata (grid layout)
- Multiple .comics files (one per piece)

## Complete Example

```tsx
import React, { useRef, useState } from 'react';
import { View, StyleSheet, TouchableOpacity, Text } from 'react-native';
import ComicsViewer, { ComicsViewerRef } from 'react-native-comics-viewer';

export default function ComicsPlayerScreen({ comicsPath }: { comicsPath: string }) {
  const comicsRef = useRef<ComicsViewerRef>(null);
  const [isPlaying, setIsPlaying] = useState(false);

  const togglePlayback = () => {
    if (isPlaying) {
      comicsRef.current?.pause();
    } else {
      comicsRef.current?.play();
    }
    setIsPlaying(!isPlaying);
  };

  return (
    <View style={styles.container}>
      <ComicsViewer
        ref={comicsRef}
        filePath={comicsPath}
        soundEnabled={true}
        onLoaded={() => {
          comicsRef.current?.play();
          setIsPlaying(true);
        }}
        onScrollChanged={(position) => {
          // Update progress bar
        }}
        onError={(error) => {
          console.error('Comics error:', error);
        }}
        style={styles.viewer}
      />

      <TouchableOpacity
        style={styles.playButton}
        onPress={togglePlayback}
      >
        <Text style={styles.buttonText}>
          {isPlaying ? 'Pause' : 'Play'}
        </Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  viewer: {
    flex: 1,
  },
  playButton: {
    position: 'absolute',
    bottom: 40,
    alignSelf: 'center',
    backgroundColor: 'rgba(0,0,0,0.7)',
    padding: 15,
    borderRadius: 25,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
});
```

## TypeScript Definitions

```typescript
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

export interface PuzzleViewerProps {
  filePath: string;
  onLoaded?: () => void;
  onPieceSelected?: (index: number) => void;
  onError?: (error: string) => void;
  soundEnabled?: boolean;
  showPreview?: boolean;
  style?: ViewStyle;
}

export interface PuzzleViewerRef {
  loadPuzzle(filePath: string): Promise<void>;
  selectPiece(index: number): void;
  play(): void;
  pause(): void;
  togglePreview(show: boolean): void;
  toggleSounds(enabled: boolean): void;
  dispose(): void;

  readonly currentPieceIndex: number;
  readonly totalPieces: number;
}
```

## Requirements

- **React Native**: 0.70+
- **React**: 18.0+
- **Android**: API 21+ (Android 5.0)
- **iOS**: 13.0+

## Bundle ID

```
net.nativemind.rn.comics.viewer
```

## License

Copyright © 2017-2026 Iron Water Studio, NativeMind. All rights reserved.
