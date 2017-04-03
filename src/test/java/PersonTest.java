import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PersonTest {
  private Person testPerson;

  @Before
  public void setUp() {
    testPerson = new Person("Henry", "henry@henry.com");
    // testPerson.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void person_instantiatesCorrectly_true(){
    assertTrue(testPerson instanceof Person);
  }

  @Test
  public void getName_personInstantiatesWithName_Henry(){
    assertEquals("Henry", testPerson.getName());
  }

  @Test
  public void getEmail_personInstantiatesWithEmail_String(){
    assertEquals("henry@henry.com", testPerson.getEmail());
  }

}
