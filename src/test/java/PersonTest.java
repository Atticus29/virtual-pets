import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class PersonTest {
  private Person testPerson;

  @Before
  public void setUp() {
    testPerson = new Person("Henry", "henry@henry.com");
    testPerson.save();
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

  @Test
  public void equals_returnsTrueIfNameAndEmailAreSame_true() {
    Person anotherPerson = new Person("Henry", "henry@henry.com");
    assertTrue(testPerson.equals(anotherPerson));
  }

  @Test
  public void all_returnsAll_returnsAllInstancesofPerson_true() {
    Person secondPerson = new Person("Harriet", "harriet@harriet.com");
    secondPerson.save();
    assertTrue(Person.all().get(0).equals(testPerson));
    assertTrue(Person.all().get(1).equals(secondPerson));
  }

  @Test
  public void save_assignsIdToObject() {
    Person savedPerson = Person.all().get(0);
    assertEquals(testPerson.getId(), savedPerson.getId());
  }

  @Test
  public void find_returnsPersonWithSameId_secondPerson(){
    Person secondPerson = new Person("Harriet", "harriet@harriet.com");
    secondPerson.save();
    assertEquals(Person.find(secondPerson.getId()), secondPerson);
  }

  @Test
  public void getMonsters_getsAllMonstersFromDatabase_monstersList() {
    Monster firstMonster = new Monster("Bubbles", testPerson.getId());
    firstMonster.save();
    Monster secondMonster = new Monster("Spud", testPerson.getId());
    secondMonster.save();
    Monster[] monsters = new Monster[] { firstMonster, secondMonster };
    assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
  }

}
