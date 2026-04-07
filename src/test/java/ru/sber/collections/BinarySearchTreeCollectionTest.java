package ru.sber.collections;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeCollectionTest {

  @Test
  void addContainsAndIterationWithDuplicates() {
    BinarySearchTreeCollection<Integer> c = new BinarySearchTreeCollection<>();
    assertTrue(c.isEmpty());

    c.add(3);
    c.add(1);
    c.add(2);
    c.add(2);

    assertEquals(4, c.size());
    assertTrue(c.contains(1));
    assertTrue(c.contains(2));
    assertFalse(c.contains(10));

    assertIterableEquals(List.of(1, 2, 2, 3), c);
  }

  @Test
  void removeDecrementsDuplicatesThenRemovesNode() {
    BinarySearchTreeCollection<Integer> c = new BinarySearchTreeCollection<>();
    c.add(2);
    c.add(2);
    c.add(1);
    c.add(3);

    assertTrue(c.remove(2));
    assertEquals(3, c.size());
    assertTrue(c.contains(2));
    assertIterableEquals(List.of(1, 2, 3), c);

    assertTrue(c.remove(2));
    assertEquals(2, c.size());
    assertFalse(c.contains(2));
    assertIterableEquals(List.of(1, 3), c);
  }

  @Test
  void clearEmptiesCollection() {
    BinarySearchTreeCollection<Integer> c = new BinarySearchTreeCollection<>();
    c.addAll(List.of(5, 4, 6));
    c.clear();
    assertEquals(0, c.size());
    assertTrue(c.isEmpty());
    assertFalse(c.contains(5));
    assertIterableEquals(List.of(), c);
  }

  @Test
  void iteratorRemoveRemovesLastReturned() {
    BinarySearchTreeCollection<Integer> c = new BinarySearchTreeCollection<>();
    c.addAll(List.of(3, 1, 2, 2));

    Iterator<Integer> it = c.iterator();
    assertEquals(1, it.next());
    it.remove(); // remove 1

    assertEquals(3, c.size());
    assertFalse(c.contains(1));
    assertIterableEquals(List.of(2, 2, 3), c);

    // remove one "2" via iterator
    assertEquals(2, it.next());
    it.remove();
    assertEquals(2, c.size());
    assertIterableEquals(List.of(2, 3), c);
  }
}

