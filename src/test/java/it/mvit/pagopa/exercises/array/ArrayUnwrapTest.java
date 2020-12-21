package it.mvit.pagopa.exercises.array;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class ArrayUnwrapTest {

    private final ArrayFlattener toBeTested = new ArrayFlattener();

    @Test
    public void testEmpty() {
        int[] result = toBeTested.flatten( new int[] {} );
        assertThat( result ).isNotNull();
        assertThat( result ).isEmpty();
    }

    @Test
    public void testIllegalArgument1() {
        assertThatThrownBy( () -> { toBeTested.flatten( 4 ); })
                .isInstanceOf( IllegalArgumentException.class );
    }

    @Test
    public void testIllegalArgument2() {
        assertThatThrownBy( () -> { toBeTested.flatten( new String[] { "" } ); })
                .isInstanceOf( IllegalArgumentException.class );
    }

    @Test
    public void testIllegalArgument3() {
        assertThatThrownBy( () -> { toBeTested.flatten( null ); })
                .isInstanceOf( IllegalArgumentException.class );
    }

    @Test
    public void testIllegalArgument4() {
        assertThatThrownBy( () -> { toBeTested.flatten( new Integer[] { 4, null} ); })
                .isInstanceOf( IllegalArgumentException.class );
    }

    @Test
    public void testSimple() {
        int[] result = toBeTested.flatten( new int[] { 2,3,4 } );
        assertThat( result ).isNotNull();
        assertThat( result ).isEqualTo( new int[] { 2,3,4 } );
    }

    @Test
    public void testOneNesting() {
        int[] result = toBeTested.flatten( new Object[] { new int[] {5,7},3,4 } );
        assertThat( result ).isNotNull();
        assertThat( result ).isEqualTo( new int[] { 5, 7,3,4 } );
    }

    @Test
    public void testMultipleNesting() {
        int[] result = toBeTested.flatten( new Object[] { Integer.valueOf(1), new Object[] {5, new Integer[] {2,5}},3,4, new Object[0] } );
        assertThat( result ).isNotNull();
        assertThat( result ).isEqualTo( new int[] { 1, 5, 2, 5, 3,4 } );
    }

    @Test
    public void testMultipleNestingMultipleLevels() {
        int[] result = toBeTested.flatten( new Object[] {
                Integer.valueOf(1),
                new Object[] {
                        5,
                        new Integer[] {2,5}
                    },
                3,
                4,
                new Object[] {
                        new int[] {10, 12},
                        13,
                        new Object[] {
                                new int[] {20, 23, 24},
                                16
                            }
                }
        });
        assertThat( result ).isNotNull();
        assertThat( result ).isEqualTo( new int[] { 1, 5, 2, 5, 3,4, 10, 12, 13, 20, 23, 24, 16 } );
    }
}
