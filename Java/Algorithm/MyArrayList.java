import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by liufengkai on 16/7/8.
 */
public class MyArrayList<AnyType> implements Iterator<AnyType> {

    private static final int DDEFAULT_CAPACITY = 10;

    private int theSize;

    private AnyType[] theItems;

    public MyArrayList() {
        clear();
    }

    public void clear() {
        theSize = 0;
        ensureCapacity(DDEFAULT_CAPACITY);
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void trimToSize() {
        ensureCapacity(size());
    }

    public AnyType get(int idx) {
        if (idx < 0 || idx >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return theItems[idx];
    }

    public AnyType set(int idx, AnyType newVal) {
        if (idx < 0 || idx >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        AnyType old = theItems[idx];
        theItems[idx] = newVal;
        return old;
    }

    public void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize)
            return;

        AnyType[] old = theItems;

        theItems = (AnyType[]) new Object[newCapacity];

        for (int i = 0; i < size(); i++) {
            theItems[i] = old[i];
        }
    }

    public boolean add(AnyType x) {
        add(size(), x);
        return true;
    }

    public void add(int index, AnyType x) {
        if (theItems.length == size()) {
            ensureCapacity(size() * 2 + 1);
        }

        for (int i = theSize; i > index; i--) {
            theItems[i] = theItems[i - 1];
        }

        theItems[index] = x;

        theSize++;
    }

    public AnyType remove(int index) {
        AnyType removeItem = theItems[index];

        for (int i = index; i < size() - 1; i++) {
            theItems[i] = theItems[i + 1];
        }

        theSize--;
        return removeItem;
    }


    public Iterator<AnyType> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<AnyType> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size();
        }

        @Override
        public AnyType next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return theItems[current++];
        }


        @Override
        public void remove() {
            MyArrayList.this.remove(--current);
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

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super AnyType> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }

    public static void main(String[] args) {
        MyArrayList<String> list = new MyArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("i = " + i);
        }

        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
