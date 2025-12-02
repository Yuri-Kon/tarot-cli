package tarot.spread;

/**
 * 牌阵中的一个位置
 * label: 用于简单显示
 * description: 用于详细说明
 */
public class CardPosition {
   private final String label;
   private final String description;

   public CardPosition(String label, String description) {
    this.label = label;
    this.description = description;
   }

   /**
    * 牌位标签，例如过去、现在、未来
    * @return
    */
   public String getLabel() {
    return label;
   }
   
   /**
    * 牌位详细说明，用于帮助解读
    * @return
    */
   public String getDescription() {
    return description;
   }
   
   @Override
   public String toString() {
    return label;
   }
}
