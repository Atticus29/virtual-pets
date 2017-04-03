import org.sql2o.*;
import java.util.List;

public class Monster {
  private String name;
  private int personId;
  private int id;
  private int foodLevel;
  private int sleepLevel;
  private int playLevel;

  public static final int MAX_FOOD_LEVEL = 3;
  public static final int MAX_SLEEP_LEVEL = 8;
  public static final int MAX_PLAY_LEVEL = 12;
  public static final int MIN_ALL_LEVELS = 0;

  public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    this.playLevel = MAX_PLAY_LEVEL / 2;
    this.sleepLevel = MAX_SLEEP_LEVEL / 2;
    this.foodLevel = MAX_FOOD_LEVEL / 2;
  }

  public String getName(){
    return name;
  }

  public int getPersonId(){
    return personId;
  }

  public int getId(){
    return this.id;
  }

  public int getPlayLevel() {
    return playLevel;
  }

  public int getSleepLevel() {
    return sleepLevel;
  }

  public int getFoodLevel() {
    return foodLevel;
  }

  @Override
  public boolean equals(Object otherMonster) {
    if(!(otherMonster instanceof Monster)) {
      return false;
    } else {
      Monster newMonster = (Monster) otherMonster;
      return this.getName().equals(newMonster.getName())
        && this.getPersonId() == newMonster.getPersonId();
    }
  }

  public void save(){
    String sqlCommand = "INSERT INTO monsters (name, personid) VALUES (:name, :personid);";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sqlCommand, true)
        .addParameter("name", this.name)
        .addParameter("personid", this.personId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Monster> all(){
    String sqlCommand = "SELECT * FROM monsters;";
    try(Connection con = DB.sql2o.open()){
      List<Monster> results = con.createQuery(sqlCommand)
        .executeAndFetch(Monster.class);
      return results;
    }
  }

  public static Monster find(int id){
    String sqlCommand = "SELECT * FROM monsters WHERE id=:id;";
    try(Connection con = DB.sql2o.open()){
      Monster result = con.createQuery(sqlCommand)
        .addParameter("id", id)
        .executeAndFetchFirst(Monster.class);
      return result;
    }
  }

  public boolean isAlive(){
    if(foodLevel <=MIN_ALL_LEVELS ||
    playLevel <=MIN_ALL_LEVELS ||
    sleepLevel <=MIN_ALL_LEVELS){
      return false;
    }
    return true;
  }

  public void depleteLevels(){
    playLevel --;
    sleepLevel --;
    foodLevel --;
  }

  public void play() {
    if(playLevel >= MAX_PLAY_LEVEL){
      throw new UnsupportedOperationException("You're playing with the monster too much!");
    }
    playLevel++;
  }

  public void sleep() {
    if(sleepLevel >=MAX_SLEEP_LEVEL){
      throw new UnsupportedOperationException("Your monster has already slept quite enough!");
    }
    sleepLevel++;
  }

  public void feed() {
    if (foodLevel >= MAX_FOOD_LEVEL) {
      throw new UnsupportedOperationException("You cannot feed your monster anymore!");
    }
    foodLevel++;
  }

}
