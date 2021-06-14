package washit.service.Account;

import io.cucumber.java.bs.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.service.AccountService;
import washit.service.exception.AccountException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestAccountServiceDeletion {

    @Mock
    private AccountRepository accRepo;

    @InjectMocks
    private AccountService service;

    private static final String EXISTING_USERNAME="Im Here";
    private static final String EXISTING_FULLNAME="Me Myself";
    private static final String EXISTING_EMAIL="mm@gmail.com";
    private static final String EXISTING_PASSWORD="Pass123!";

    private static final String NONEXIST_USERNAME="Im NotHere";

    @BeforeEach
    public void setup(){
        lenient().when(accRepo.existsByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
           if (invocation.getArgument(0).equals(EXISTING_USERNAME)){
               return true;
           }
           else{
               return false;
           }
        });

        lenient().when(accRepo.findByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
            Account acc = new Account();
            acc.setUsername(EXISTING_USERNAME);
            acc.setFullName(EXISTING_FULLNAME);
            acc.setEmail(EXISTING_EMAIL);
            acc.setPassword(EXISTING_PASSWORD);
            return acc;
        });

        lenient().doAnswer((i)->null).when(accRepo).delete(any(Account.class));
    }

    @Test
    public void testDeleteExistingAccount(){
         try {
            service.deleteAccountByUsername(EXISTING_USERNAME);
        }
        catch(AccountException e){
            fail();
        }
    }

    @Test
    public void testDeleteNonExistingAccount(){
        String error = null;
        try {
            service.deleteAccountByUsername(NONEXIST_USERNAME);
        }
        catch (AccountException e){
            error=e.getMessage();
        }
        assertEquals(error, "No account with username \""+NONEXIST_USERNAME+"\" exists");
    }
}
