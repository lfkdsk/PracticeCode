import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by liufengkai on 16/7/9.
 */
public class MyLinkedList<AnyType> implements Iterator<AnyType> {


    private static class Node<AnyType> {

        public AnyType data;

        public Node<AnyType> prev;

        public Node<AnyType> next;

        public Node(AnyType data,
                    Node<AnyType> prev,
                    Node<AnyType> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

    }

    private int theSize;

    private int modCount;

    private Node<AnyType> beginMarker;

    private Node<AnyType> endMarker;


    public MyLinkedList() {
        clear();
    }

    public void clear() {
        beginMarker = new Node<>(null, null, null);
        endMarker = new Node<>(null, beginMarker, null);
        beginMarker.next = endMarker;

        theSize = 0;

        modCount++;
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean add(AnyType newVal) {
        add(size(), newVal);
        return true;
    }

    public void add(int index, AnyType newVal) {
        addBefore(getNode(index), newVal);
    }

    public AnyType get(int index) {
        return getNode(index).data;
    }

    public AnyType set(int index, AnyType newVal) {
        Node<AnyType> p = getNode(index);

        AnyType oldVal = p.data;

        p.data = newVal;

        return oldVal;
    }

    public AnyType remove(int index) {
        return remove(getNode(index));
    }

    private void addBefore(Node<AnyType> p, AnyType x) {
        Node<AnyType> newNode = new Node<>(x, p.prev, p);

        newNode.prev.next = newNode;

        p.prev = newNode;

        theSize++;

        modCount++;
    }


    private AnyType remove(Node<AnyType> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;

        theSize--;
        modCount++;

        return p.data;
    }

    private Node<AnyType> getNode(int index) {
        Node<AnyType> p;

        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        if (index < size() / 2) {
            p = beginMarker.next;

            for (int i = 0; i < index; i++) {
                p = p.next;
            }
        } else {

            p = endMarker;

            for (int i = size(); i < index; i--) {
                p = p.prev;
            }
        }

        return p;
    }

    public Iterator<AnyType> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<AnyType> {

        private Node<AnyType> current = beginMarker.next;

        private int expectedModCount = modCount;

        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return current != endMarker;
        }

        @Override
        public AnyType next() {
            // 这是在比较修改数 如果在使用迭代器的过程中修改了
            // 就会被甩出错误
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            AnyType nextItem = current.data;

            current = current.next;
            // 在这里直接亲定了可以修改移除
            okToRemove = true;

            return nextItem;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            if (!okToRemove) {
                throw new IllegalStateException();
            }

            MyLinkedList.this.remove(current.prev);

            okToRemove = false;

            expectedModCount++;
        }
    }

    @Override

    public boolean hasNext() {
        return false;
    }

    @Override
    public AnyType next() {
        return null;
    }

    public static void main(String[] args) {
        MyLinkedList<String> linkedList = new MyLinkedList<>();

        for (int i = 0; i < 10; i++) {
            linkedList.add("i = " + i);
        }

        Iterator<String> iterator = linkedList.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
