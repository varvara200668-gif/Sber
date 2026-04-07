# Sber

## Проверочное задание: своя коллекция на дереве

В проект добавлена коллекция `BinarySearchTreeCollection<E>` на базе **двоичного дерева поиска**:

- **Интерфейс**: реализует `java.util.Collection<E>`
- **Операции**: `add`, `remove`, обход через `iterator()`
- **Задание со звёздочкой**: реализован `contains`
- **Дубликаты**: поддерживаются (в узле хранится счётчик)
- **Порядок итерации**: in-order (элементы идут в отсортированном порядке)

Код:
- `src/main/java/ru/sber/collections/BinarySearchTreeCollection.java`
- демо-запуск: `src/main/java/ru/sber/Main.java`

### Как запустить (без Maven)

Если Maven не установлен, можно собрать через `javac`:

```bash
powershell -NoProfile -Command "javac --release 8 -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java -Path 'src/main/java' | Select-Object -ExpandProperty FullName)"
java -cp out ru.sber.Main
```

### Тесты

Есть JUnit-тесты в `src/test/java/...` (для запуска нужен Maven/Gradle).