# React Native Comics Viewer

React Native модуль для отображения интерактивных комиксов и пазлов с анимациями и звуком.

## Возможности

- **Рендеринг комиксов**: Отображение анимированных комиксов с автоматической прокруткой и синхронизацией звука
- **Поддержка пазлов**: Рендеринг интерактивных сеток пазлов с навигацией по кусочкам
- **Воспроизведение звука**: Аудио синхронизированное с позицией прокрутки
- **Кросс-платформенность**: Работает на Android и iOS
- **Простой API**: Всего 5 основных методов для управления воспроизведением комиксов
- **Поддержка TypeScript**: Полные TypeScript определения включены

## Установка

```bash
npm install react-native-comics-viewer
# или
yarn add react-native-comics-viewer
```

Для iOS, установите pods:

```bash
cd ios && pod install
```

## Быстрый старт

```tsx
import React, { useRef } from 'react';
import ComicsViewer, { ComicsViewerRef } from 'react-native-comics-viewer';

function ComicsScreen() {
  const comicsRef = useRef<ComicsViewerRef>(null);

  return (
    <ComicsViewer
      ref={comicsRef}
      filePath="/путь/к/episode.comics"
      onLoaded={() => {
        console.log('Комикс загружен!');
        comicsRef.current?.play();
      }}
      onScrollChanged={(position) => {
        console.log('Позиция прокрутки:', position);
      }}
      onError={(error) => {
        console.error('Ошибка:', error);
      }}
    />
  );
}
```

## Comics API

### ComicsViewer Компонент

Главный компонент для отображения комиксов.

```tsx
<ComicsViewer
  ref={comicsRef}
  filePath={string}              // Путь к .comics файлу (обязательный)
  onLoaded={() => void}          // Вызывается когда комикс загружен
  onScrollChanged={(position: number) => void}  // Позиция прокрутки изменилась
  onError={(error: string) => void}  // Произошла ошибка
  languageIndex={number}         // Выбор языка (по умолчанию: 0)
  soundEnabled={boolean}         // Включить/выключить звуки (по умолчанию: true)
  showPreview={boolean}          // Показать/скрыть превью слои (по умолчанию: false)
/>
```

### ComicsViewerRef

Интерфейс ссылки для управления комиксами.

#### Методы

**1. Загрузка и отображение**

```typescript
// Загрузить .comics файл
await comicsRef.current?.loadComics('/путь/к/файлу.comics');
```

**2. Управление воспроизведением**

```typescript
// Запустить авто-прокрутку
comicsRef.current?.play();

// Поставить на паузу
comicsRef.current?.pause();
```

**3. Навигация**

```typescript
// Установить позицию прокрутки (от 0.0 до duration)
comicsRef.current?.setScrollPosition(500.0);

// Получить текущую позицию прокрутки
const position = comicsRef.current?.getScrollPosition();
```

**4. Превью и звук**

```typescript
// Переключить видимость превью слоёв
comicsRef.current?.togglePreview(true);   // Показать превью
comicsRef.current?.togglePreview(false);  // Скрыть превью

// Переключить воспроизведение звука
comicsRef.current?.toggleSounds(true);    // Включить звуки
comicsRef.current?.toggleSounds(false);   // Выключить звуки
```

**5. Очистка ресурсов**

```typescript
// Освободить ресурсы (вызывать при размонтировании)
comicsRef.current?.dispose();
```

#### Свойства (только чтение)

```typescript
// Проверить играет ли сейчас
const playing = comicsRef.current?.isPlaying;

// Получить общую высоту прокрутки
const totalHeight = comicsRef.current?.duration;

// Получить текущую позицию
const currentPos = comicsRef.current?.currentPosition;
```

## Puzzle API

### PuzzleViewer Компонент

Компонент для отображения интерактивных пазлов.

```tsx
<PuzzleViewer
  ref={puzzleRef}
  filePath={string}              // Путь к .puzzle файлу (обязательный)
  onLoaded={() => void}
  onPieceSelected={(index: number) => void}  // Выбран кусочек по индексу
  onError={(error: string) => void}
  soundEnabled={boolean}
  showPreview={boolean}
/>
```

### PuzzleViewerRef

Интерфейс ссылки для управления пазлом.

```typescript
// Загрузить puzzle файл
await puzzleRef.current?.loadPuzzle('/путь/к/puzzle.puzzle');

// Перейти к кусочку по индексу
puzzleRef.current?.selectPiece(5);

// Получить текущий кусочек
const currentPiece = puzzleRef.current?.currentPieceIndex;

// Получить общее количество кусочков
const totalPieces = puzzleRef.current?.totalPieces;

// Управление воспроизведением текущего кусочка
puzzleRef.current?.play();
puzzleRef.current?.pause();

// Переключить превью/звуки для всех кусочков
puzzleRef.current?.togglePreview(true);
puzzleRef.current?.toggleSounds(false);

// Очистка
puzzleRef.current?.dispose();
```

## Формат файлов

### .comics Файл

Файл .comics - это ZIP архив, содержащий:

- `data.json` - Структура и метаданные комикса
- `layers/` - Изображения слоёв (PNG)
- `sounds/` - Аудио файлы (MP3)

**Пример использования:**

```typescript
// Из bundle приложения (iOS)
const filePath = 'file://' + RNFS.MainBundlePath + '/episode1.comics';

// Из директории документов
const filePath = RNFS.DocumentDirectoryPath + '/episode1.comics';

// Из скачанного файла (Android)
const filePath = '/data/user/0/com.app/files/episode1.comics';
```

### .puzzle Файл

Файл .puzzle - это ZIP архив, содержащий:

- Метаданные пазла (сетка расположения)
- Множество .comics файлов (по одному на кусочек)

## Полный пример

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
          // Обновить прогресс-бар
        }}
        onError={(error) => {
          console.error('Ошибка комикса:', error);
        }}
        style={styles.viewer}
      />

      <TouchableOpacity
        style={styles.playButton}
        onPress={togglePlayback}
      >
        <Text style={styles.buttonText}>
          {isPlaying ? 'Пауза' : 'Играть'}
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

## TypeScript Определения

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

## Требования

- **React Native**: 0.70+
- **React**: 18.0+
- **Android**: API 21+ (Android 5.0)
- **iOS**: 13.0+

## Bundle ID

```
net.nativemind.rn.comics.viewer
```

## Лицензия

Copyright © 2017-2026 Iron Water Studio, NativeMind. Все права защищены.
