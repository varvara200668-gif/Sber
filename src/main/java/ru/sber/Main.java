package ru.sber;

import ru.sber.collections.BinarySearchTreeCollection;

public final class Main {
  public static void main(String[] args) {
    BinarySearchTreeCollection<Integer> c = new BinarySearchTreeCollection<>();
    c.add(5);
    c.add(2);
    c.add(7);
    c.add(2);

    System.out.println("size=" + c.size());          // 4
    System.out.println("contains(7)=" + c.contains(7)); // true
    System.out.print("iterate: ");
    for (Integer v : c) System.out.print(v + " ");   // 2 2 5 7
    System.out.println();

    c.remove(2);
    System.out.println("after remove(2), size=" + c.size()); // 3
  }
}

