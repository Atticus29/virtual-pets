import org.sql2o.*;
import java.util.List;

public class Monster {
  private String name;
  private int personId;
  private int id;

  public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
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

}
