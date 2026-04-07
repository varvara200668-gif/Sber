package ru.sber.collections;

import java.util.*;

public final class BinarySearchTreeCollection<E extends Comparable<? super E>> implements Collection<E> {

  private static final class Node<E> { E v; Node<E> l; Node<E> r; int c=1; Node(E v){this.v=v;} }

  private Node<E> root; private int size; private int modCount;

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean contains(Object o) {
    if(o==null) return false;
    E x;
    try{ x = (E)o; } catch (Exception e){ return false; }
    Node<E> n = root;
    while(n!=null){
      int k = x.compareTo(n.v);
      if(k==0) return true;
      if(k<0) n=n.l; else n=n.r;
    }
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return new It();
  }

  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size];
    int i = 0;
    for (E e : this) {
      arr[i++] = e;
    }
    return arr;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    if(a==null) throw new NullPointerException();
    final int n=size;
    final T[] out = a.length >= n ? a : Arrays.copyOf(a, n);
    int i = 0;
    for (E e : this) {
      out[i++] = (T)e;
    }
    if (out.length > n) out[n] = null;
    return out;
  }

  @Override
  public boolean add(E e) {
    if(e==null) throw new NullPointerException();
    if(root==null){ root=new Node<E>(e); size=1; modCount++; return true; }
    Node<E> n=root;
    while(true){
      int k=e.compareTo(n.v);
      if(k==0){ n.c++; size++; modCount++; return true; }
      if(k<0){
        if(n.l==null){ n.l=new Node<E>(e); size++; modCount++; return true; }
        n=n.l;
      } else {
        if(n.r==null){ n.r=new Node<E>(e); size++; modCount++; return true; }
        n=n.r;
      }
    }
  }

  @Override
  public boolean remove(Object o) {
    if(o==null) return false;
    if(root==null) return false;
    E x;
    try{ x=(E)o; }catch(Exception e){ return false; }
    RemovalResult<E> rr = removeNode(root, x);
    if(!rr.removed) return false;
    root = rr.newRoot;
    size--;
    modCount++;
    return true;
  }

  private static final class RemovalResult<E> {
    final Node<E> newRoot;
    final boolean removed;

    RemovalResult(Node<E> newRoot, boolean removed) {
      this.newRoot = newRoot;
      this.removed = removed;
    }
  }

  private RemovalResult<E> removeNode(Node<E> node, E value) {
    if (node == null) return new RemovalResult<>(null, false);

    int cmp = value.compareTo(node.v);
    if (cmp < 0) {
      RemovalResult<E> res = removeNode(node.l, value);
      node.l = res.newRoot;
      return new RemovalResult<>(node, res.removed);
    }
    if (cmp > 0) {
      RemovalResult<E> res = removeNode(node.r, value);
      node.r = res.newRoot;
      return new RemovalResult<>(node, res.removed);
    }

    // cmp == 0
    if (node.c > 1) {
      node.c--;
      return new RemovalResult<>(node, true);
    }

    if (node.l == null) return new RemovalResult<>(node.r, true);
    if (node.r == null) return new RemovalResult<>(node.l, true);

    // два ребёнка: заменяем минимальным из правого поддерева
    MinExtractResult<E> extracted = extractMin(node.r);
    Node<E> successor = extracted.min;
    successor.l = node.l;
    successor.r = extracted.newRoot;
    return new RemovalResult<>(successor, true);
  }

  private static final class MinExtractResult<E> {
    final Node<E> min;
    final Node<E> newRoot;

    MinExtractResult(Node<E> min, Node<E> newRoot) {
      this.min = min;
      this.newRoot = newRoot;
    }
  }

  private MinExtractResult<E> extractMin(Node<E> node) {
    if (node.l == null) {
      return new MinExtractResult<>(node, node.r);
    }
    MinExtractResult<E> res = extractMin(node.l);
    node.l = res.newRoot;
    return new MinExtractResult<>(res.min, node);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    if(c==null) throw new NullPointerException();
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    if(c==null) throw new NullPointerException();
    boolean changed = false;
    for (E e : c) { if(add(e)) changed = true; }
    return changed;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    if(c==null) throw new NullPointerException();
    boolean changed = false;
    for (Object o : c) {
      while (remove(o)) {
        changed = true;
      }
    }
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    if(c==null) throw new NullPointerException();
    boolean changed = false;
    ArrayList toRemove = new ArrayList();
    for (E v : this) {
      if (!c.contains(v)) toRemove.add(v);
    }
    for (Object v : toRemove) {
      if (remove(v)) changed = true;
    }
    return changed;
  }

  @Override
  public void clear() {
    root=null; size=0; modCount++;
  }

  private final class It implements Iterator<E> {
    private ArrayList list;
    private int i=0;
    private int last=-1;
    private int exp;

    It(){
      exp = modCount;
      list = new ArrayList();
      ArrayDeque st = new ArrayDeque();
      Node<E> cur = root;
      while(cur!=null || !st.isEmpty()){
        while(cur!=null){
          st.push(cur);
          cur = cur.l;
        }
        Node<E> n = (Node<E>) st.pop();
        for(int j=0;j<n.c;j++) list.add(n.v);
        cur = n.r;
      }
    }

    public boolean hasNext(){ return i < list.size(); }

    public E next(){
      if(modCount!=exp) throw new ConcurrentModificationException();
      if(!hasNext()) throw new NoSuchElementException();
      last = i;
      return (E) list.get(i++);
    }

    public void remove(){
      if(modCount!=exp) throw new ConcurrentModificationException();
      if(last<0) throw new IllegalStateException();
      Object v = list.get(last);
      BinarySearchTreeCollection.this.remove(v);
      exp = modCount;
      list.remove(last);
      if(last<i) i--;
      last=-1;
    }
  }
}

