import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;

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

  @Test
  public void find_returnsMonsterWithSameId_secondMonster(){
    Monster secondMonster = new Monster("Spud", 3);
    secondMonster.save();
    assertEquals(Monster.find(secondMonster.getId()), secondMonster);
  }

  @Test
  public void monster_instantiatesWithHalfFullLevels() {
    assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL/2));
    assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL/2));
    assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL/2));
  }

  @Test
  public void isAlive_confirmsMonsterIsAliveIFAllLevelsAboveMinimum_true(){
    assertTrue(testMonster.isAlive());
  }

  @Test
  public void depleteLevels_reducesAllLevels(){
    testMonster.depleteLevels();
    assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL/2)-1);
    assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL/2)-1);
    assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL/2)-1);
  }

  @Test
  public void isAlive_recognizesMonsterIsDeadWhenLevelsReachMinimum_false(){
    for(int i = Monster.MIN_ALL_LEVELS; i<=Monster.MAX_FOOD_LEVEL; i++){
      testMonster.depleteLevels();
    }
    assertEquals(testMonster.isAlive(), false);
  }

  @Test
  public  void play_sleep_feed_increasesLevels() {
    testMonster.play();
    testMonster.sleep();
    testMonster.feed();
    assertTrue(testMonster.getPlayLevel() > (Monster.MAX_PLAY_LEVEL/2));
    assertTrue(testMonster.getSleepLevel() > (Monster.MAX_SLEEP_LEVEL/2));
    assertTrue(testMonster.getFoodLevel() > (Monster.MAX_FOOD_LEVEL/2));
  }

  @Test
  public void monster_levelsCannotGoBeyondMaxValues() {
    for (int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_PLAY_LEVEL + 2); i++) {
      try {
        testMonster.feed();
        testMonster.play();
        testMonster.sleep();
      } catch (UnsupportedOperationException exception) { }
    }
    assertTrue(testMonster.getFoodLevel() <= Monster.MAX_FOOD_LEVEL);
    assertTrue(testMonster.getPlayLevel() <= Monster.MAX_PLAY_LEVEL);
    assertTrue(testMonster.getSleepLevel() <= Monster.MAX_SLEEP_LEVEL);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void feed_throwsExceptionIfFoodLevelIsAtMaxValue() {
    for (int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_FOOD_LEVEL); i++) {
      testMonster.feed();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void play_throwsExceptionIfPlayLevelIsAtMaxValue() {
    for (int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_PLAY_LEVEL); i++) {
      testMonster.play();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void sleep_throwsExceptionIfSleepLevelIsAtMaxValue(){
    for (int i = Monster.MIN_ALL_LEVELS; i<=(Monster.MAX_SLEEP_LEVEL)+20; i++ ) {
      testMonster.sleep();
    }
  }

  @Test
  public void save_recordsTimeOfCreationInDatabase() {
    Timestamp savedMonsterBirthday = Monster.find(testMonster.getId()).getBirthday();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), savedMonsterBirthday.getDay());
  }

  @Test
  public void action_recordsTimeLastActionInDatabase_true(){
    testMonster.sleep();
    testMonster.feed();
    testMonster.play();
    Timestamp savedMonsterLastSlept = Monster.find(testMonster.getId()).getLastSlept();
    Timestamp savedMonsterLastAte = Monster.find(testMonster.getId()).getLastAte();
    Timestamp savedMonsterLastPlayed = Monster.find(testMonster.getId()).getLastPlayed();
    Timestamp rightNow = new Timestamp (new Date().getTime());
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastSlept));
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastAte));
    assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastPlayed));
  }

  @Test
  public void timer_executesDepleteLevelsMethod(){
    Monster testMonster2 = new Monster("Bubbles", 1);
    int firstPlayLevel = testMonster2.getPlayLevel();
    testMonster2.startTimer();
    try{
      Thread.sleep(5000);
    } catch (InterruptedException exception){}
    int secondPlayLevel = testMonster2.getPlayLevel();
    assertTrue(firstPlayLevel > secondPlayLevel);
  }

  @Test
  public void timer_haltsAfterMonsterDies(){
    Monster testMonster2 = new Monster("Bubbles", 1);
    testMonster2.startTimer();
    try{
      Thread.sleep(5000);
    } catch (InterruptedException exception){}
      System.out.println(testMonster2.getFoodLevel());
      assertFalse(testMonster2.isAlive());
      assertTrue(testMonster2.getFoodLevel() >=0);

  }

}
