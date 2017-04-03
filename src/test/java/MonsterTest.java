import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class MonsterTest {
  private Monster testMonster;

  @Before
  public void setUp() {
    testMonster = new Monster("Bubbles", 1);
    testMonster.save();
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void monster_instantiatesCorrectly_true() {
    assertTrue(testMonster instanceof Monster);
  }

  @Test
  public void monster_instantiatesWithName_String() {
    assertEquals("Bubbles", testMonster.getName());
  }

  @Test
  public void monster_instantiatesWithPersonId_int() {
    assertEquals(1, testMonster.getPersonId());
  }

  @Test
  public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
    Monster anotherMonster = new Monster("Bubbles", 1);
    assertTrue(testMonster.equals(anotherMonster));
  }

  @Test
  public void save_assignsIdToMonster_true(){
    Monster savedMonster = Monster.all().get(0);
    assertEquals(savedMonster.getId(), testMonster.getId());
  }

  @Test
  public void all_returnsAllInstancesOfMonster_true(){
    Monster secondMonster = new Monster ("Spud", 1);
    secondMonster.save();
    assertTrue(Monster.all().get(0).equals(testMonster));
    assertTrue(Monster.all().get(1).equals(secondMonster));
  }

}
