/**
 * Authors: Clayton Williams and Emalina Bidari
 * Lab9: Doubly Linked List
 */

import java.util.AbstractSequentialList;
import java.util.*;

/**
 * Doubly Linked List Class contains inner classes Node and ListIterator. Today, in lab, we
 * were informed by Nakai that this convoluted way of designing the class is to ensure it is easier
 * to modify in the future(something about decoupling stuff).
 */
public class DoublyLinkedList<E> extends AbstractSequentialList<E> implements List<E> {
    private int size = 0;
    private Node<E> first;
    private int modCount = 0;

    /**
     * Constructor for an empty DoublyLinkedList. Sets the 'first' pointer to null
     * since the list is empty.
     */
    public DoublyLinkedList(){
        first = null;
    }

    /**
     * Returns the size of a doubly linked list
     * @return size number of nodes in the doubly linked list
     */
    public int size(){
        return size;
    }

    /**
     * Creates a listIterator object that traverses the list forwards and backwards starting at the
     * given index
     * @param index index to start the list iteratpr at
     * @return list iterator object that allows us to move in the list
     */
    public ListIterator<E> listIterator(int index) {
        //creates an iterator object
        if ((index < 0) || (index > size))
        {
            throw new IndexOutOfBoundsException();
        }

        DoublyLinkedListIter iter = new DoublyLinkedListIter();
        //iterate to index
        for (int i = 0; i != index; i++)
        {
            iter.next();
        }

        return iter;
    }

    /**
     *Inner Node class for a Node object. Node object just stores the data(e) and has pointers
     * to the next and previous nodes
     */
    private class Node<E>
    {
        public Node<E> prev;
        public Node<E> next;
        public E data;

        /**
         * Constructor for a node. Takes in some data to be stored in the node.
         * @param data
         */
        public Node(E data){
            this.data = data;
            prev = null;
            next = null;
        }
    }

    /**
     * Inner class for a list iterator object. Allows us to add,remose,set and traverse the list both
     * forward and backwards(since it's doubly linked).
     */
    private class DoublyLinkedListIter implements ListIterator<E> {
        public Node<E> iprev;
        public Node<E> inext;
        public Node<E> ret;
        public int index;
        public boolean isAfterNextOrPrev;


        /**
         * Constructor for a doubly linked list iterator. Sets the boolean value to false since next or prev
         * hasnt been called. Sets inext to the first value and iprev to null(first node doesnt have a previous
         * cause the list isnt circularly linked)
         */
        public DoublyLinkedListIter(){
            index = 0;
            inext = first;
            iprev = null;
            isAfterNextOrPrev = false;
        }

        /**
         * Checks if the iterator has a next node to "swing" to
         * @return true if there is a next node, false otherwise
         */
        public boolean hasNext(){
            return (inext != null);
        }

        /**
         * Checks if iterator has a prev node to 'swing' to
         * @return true if there is, false otherwise
         */
        public boolean hasPrevious(){
            return (iprev != null);
        }

        /**
         * Moves the iterator one index up(->). returns data contained in the previous node
         * @return
         */
        public E next(){
            if ((index < 0) || (index > size))
            {
                throw new IndexOutOfBoundsException();
            }
            isAfterNextOrPrev = true;
            if (!hasNext()){return null;}
            ret = iprev;
            inext = inext.next;
            iprev = iprev.next;
            index++;
            return iprev.data;
        }

        /**
         * Moves the iterator one index down.(<-) returns data contained in the next node
         * @return
         */
        public E previous(){
            if ((index < 0) || (index > size))
            {
                throw new IndexOutOfBoundsException();
            }
            isAfterNextOrPrev = true;
            if (!hasPrevious()) {return null;}
            ret = inext;
            inext = iprev;
            iprev = iprev.prev;
            index--;
            return inext.data;
        }

        /**
         * Returns index of the next node the iterator will 'swing' to
         * @return int index
         */
        public int nextIndex(){
            return index;
        }

        /**
         * Returns index of the previous node the iterator will 'swing' to
         * @return int previous index
         */
        public int previousIndex(){
            return index-1;
        }

        /**
         * Inserts the specified element into the list.
         * The element is inserted immediately before the element that would be returned by next(),
         * if any, and after the element that would be returned by previous(), if any. (If the list contains no elements,
         * the new element becomes the sole element on the list.) The new element is inserted before the implicit cursor: a
         * subsequent call to next would be unaffected, and a subsequent call to previous would return the new element. (
         * This call increases by one the value that would be returned by a call to nextIndex or previousIndex.)
         * @param e
         */
        public void add(E e){
            isAfterNextOrPrev = false;
            Node newNode = new Node(e);
            if (size == 0)
            { //if there is no node
                first = newNode;
                inext = newNode;
                iprev = null;
            }
            else //if linked list is not empty
            {
                //next node prev set as nodes current previous
                newNode.next = inext;
                newNode.prev = iprev;
                inext.prev = newNode;
                iprev.next = newNode;
            }
            index++;
        }
        /**
         * Removes from the list the last element that was returned by next() or previous() (optional operation).
         * This call can only be made once per call to next or previous. It can be made only if add(E) has not been
         * called after the last call to next or previous. We made this possible by having the boolean value isAfterNextorPrevious.
         * UnsupportedOperationException - if the remove operation is not supported by this list iterator
         * IllegalStateException - if neither next nor previous have been called, or remove or add have been
         * called after the last call to next or previous
         */
        public void remove(){
            if (size == 0){
                throw new UnsupportedOperationException();
            }
            if (!isAfterNextOrPrev){
                //if add was called
                throw new IllegalStateException();
            }
            else{
                //if next or previous was called most recently
                ret.prev.next = ret.next;
                ret.next.prev = ret.prev;
            }
            isAfterNextOrPrev = false;
            index--;
        }

        /**
         * /**
         * Replaces the last element returned by next() or previous() with the specified element.
         * This call can be made only if neither remove() nor add(E) have been called after the last call to next or previous.
         * We made this possible by having the boolean value isAfterNextorPrevious.
         Throws:
         UnsupportedOperationException - if the set operation is not supported by this list iterator
         ClassCastException - if the class of the specified element prevents it from being added to this list
         IllegalArgumentException - if some aspect of the specified element prevents it from being added to this list
         IllegalStateException - if neither next nor previous have been called, or remove or add have been called after the last call to next or previous
         */
        public void set(E e){
            if (size == 0){
                throw new UnsupportedOperationException();
            }

            if(isAfterNextOrPrev){
                ret.data = e;
            }
            else {
                throw new IllegalStateException();
            }
        }


    }
}
