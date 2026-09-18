package debug;

import java.util.ArrayList;
import protonova.protobuf.DamageProto.Damage;
import protonova.protobuf.DamageProto.DamageMultiplier;
import protonova.protobuf.EntityProto.Entity;
import util.DamageMultiplierUtil;

public class DamageMultiplierTest {

  public static void main(String[] args) {
    Entity entity =
        Entity.newBuilder()
            .setDamage(
                Damage.newBuilder()
                    .setDamageMultiplier(
                        DamageMultiplier.newBuilder()
                            .setAsphyxiation(2f)
                            .setBleeding(2f)
                            .setBurn(2f)
                            .setGenetic(2f)
                            .build())
                    .build())
            .build();

    Entity entity2 =
        Entity.newBuilder()
            .setDamage(
                Damage.newBuilder()
                    .setDamageMultiplier(
                        DamageMultiplier.newBuilder()
                            .setAsphyxiation(3f)
                            .setBleeding(3f)
                            .setBurn(3f)
                            .setStructural(1.5f)
                            .build())
                    .build())
            .build();
    ArrayList<Entity> list = new ArrayList<>();
    list.add(entity2);

    DamageMultiplier multi = DamageMultiplierUtil.getTotalDamageMultiplier(entity, list);

    System.out.println("Brute: " + multi.getBrute());
    System.out.println("burn: " + multi.getBurn());
    System.out.println("struc: " + multi.getStructural());
    System.out.println("genetic: " + multi.getGenetic());
    System.out.println("Asphyxiation: " + multi.getAsphyxiation());
    System.out.println("Toxin: " + multi.getToxin());
    System.out.println("Bleeding: " + multi.getBleeding());
  }
}
