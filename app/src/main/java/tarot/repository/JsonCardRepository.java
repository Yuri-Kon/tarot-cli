package tarot.repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tarot.domain.Arcana;
import tarot.domain.Suit;
import tarot.domain.TarotCard;

public class JsonCardRepository implements CardRepository {

  private final String resourcePath;

  private final Gson gson = new Gson();

  public JsonCardRepository() {
    this("/tarot/cards.json");
  }

  public JsonCardRepository(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  @Override
  public List<TarotCard> findAll() {
    try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IllegalStateException("资源文件 " + resourcePath + " 不存在");
      }
      try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
        TarotCardJson[] dtoArray = gson.fromJson(reader, TarotCardJson[].class);
        if (dtoArray.length == 0) {
          throw new IllegalStateException("JSON解析结果为空：" + resourcePath);
        }
        return mapToDomain(Arrays.asList(dtoArray));
      }
    } catch (JsonSyntaxException e) {
      throw new IllegalStateException("JSON 语法错误：" + resourcePath, e);
    } catch (Exception e) {
      throw new RuntimeException("加载塔罗牌JSON失败：" + resourcePath, e);
    }
  }

  private List<TarotCard> mapToDomain(List<TarotCardJson> dtoList) {
    List<TarotCard> result = new ArrayList<>();
    for (TarotCardJson dto : dtoList) {
      Arcana arcana = Arcana.valueOf(dto.arcana);
      Suit suit = Suit.valueOf(dto.suit);
      TarotCard card = new TarotCard(dto.id, dto.name, arcana, suit, dto.number);
      result.add(card);
    }
    return result;
  }

  /** 与Json字段对应的简单DTO */
  private static class TarotCardJson {
    String id;
    String name;
    String nameEn;
    String arcana;
    String suit;
    int number;
  }
}
