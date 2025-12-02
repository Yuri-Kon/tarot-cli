package tarot.repository;

import java.util.List;
import tarot.domain.TarotCard;

/**
 * 牌数据的仓储接口
 * 目前用JSON实现，将来可以换成数据库等
 */
public interface CardRepository {

    /**
     * 返回一副标准塔罗牌所包含的所有牌定义
     * 当前版本只可以返回部分牌用于测试
     * @return
     */
   List<TarotCard> findAll(); 
}
