import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.*;
public class UnitTests {
    private SecurityService security = SecurityService.getInstance();
    @Test
    public void testAlphanumeric(){
        assertTrue(security.checkAlphaNumeric("Jonas"));
        assertFalse(security.checkAlphaNumeric("#Jonas"));
        assertFalse(security.checkAlphaNumeric(""));
        assertFalse(security.checkAlphaNumeric("Jo_nas"));
        assertFalse(security.checkAlphaNumeric("Jonas#"));
        assertFalse(security.checkAlphaNumeric("Jo nas"));
    }
}