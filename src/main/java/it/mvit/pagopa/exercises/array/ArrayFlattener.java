package it.mvit.pagopa.exercises.array;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayFlattener {


    public int[] flatten( Object array) {
        assertNotNull( array );
        assertIsArray( array );

        Stack<Object> stack = new Stack<>();
        stack.push( array );

        List<Integer> result = new LinkedList<>();
        while( ! stack.isEmpty() ) {
            Object el = stack.pop();
            assertNotNull( el );

            Class type = el.getClass();
            if( type.isArray() ) {
                addArrayToStack( stack, el);
            }
            else {
                Integer value = validateElement( el );
                result.add( value );
            }
        }

        return fromListOfInteger2ArrayOfInt( result );
    }

    private void addArrayToStack(Stack<Object> stack, Object el) {
        int length = Array.getLength(el);

        // To keep sibling output order I have to put ir into stack in reverse order
        for( int idx = length-1; idx >=  0; idx-- ) {
            stack.push( Array.get(el, idx ));
        }
    }

    private Integer validateElement(Object el) {
        if( ! (el instanceof Integer)) {
            throwWrongTypeException( el.getClass() );
        }
        return (Integer) el;
    }

    protected void assertIsArray(Object obj) {
        Class type = obj.getClass();
        if( ! type.isArray() ) {
            throwWrongTypeException(type);
        }
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throwNoNullObjects();
        }
    }

    private void throwWrongTypeException(Class type) {
        throw new IllegalArgumentException( type + " is not supported. This code supports only combination of arrays, Integer and int");
    }

    private void throwNoNullObjects() {
        throw new IllegalArgumentException(" null objects are not supported");
    }

    private int[] fromListOfInteger2ArrayOfInt(List<Integer> list) {
        int[] array = new int[ list.size() ];
        int arrayIndex = 0;
        for( Integer value : list ) {
            array[ arrayIndex++ ] = value;
        }
        return array;
    }
}
