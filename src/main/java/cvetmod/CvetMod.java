package cvetmod;

import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.sun.org.apache.xpath.internal.operations.Or;
import cvetmod.cards.special.Originium;
import cvetmod.cards.special.TheRealityOfEnd;
import cvetmod.patches.CvetEnum;
import cvetmod.util.SecondEnergyIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cvetmod.cards.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static cvetmod.patches.AbstractCardEnum.CVET_PINK;

@SpireInitializer
public class CvetMod implements EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, PostBattleSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, AddCustomModeModsSubscriber, OnStartBattleSubscriber, OnPlayerLoseBlockSubscriber, RelicGetSubscriber {

    public static final Color CvetPink = CardHelper.getColor(249, 218, 212);
    private static final String attackCard = "cvetmod/images/512/bg_attack_cvet.png";
    private static final String skillCard = "cvetmod/images/512/bg_skill_cvet.png";
    private static final String powerCard = "cvetmod/images/512/bg_power_cvet.png";
    private static final String energyOrb = "cvetmod/images/512/card_cvet_orb.png";
    private static final String attackCardPortrait = "cvetmod/images/1024/bg_attack_cvet.png";
    private static final String skillCardPortrait = "cvetmod/images/1024/bg_skill_cvet.png";
    private static final String powerCardPortrait = "cvetmod/images/1024/bg_power_cvet.png";
    private static final String energyOrbPortrait = "cvetmod/images/1024/card_cvet_orb.png";
    private static final String charButton = "cvetmod/images/charSelect/button.png";
    private static final String charPortrait = "cvetmod/images/charSelect/portrait.png";
    private static final String miniManaSymbol = "cvetmod/images/ManaAmiya.png";
    private static final Logger logger = LogManager.getLogger(cvetmod.CvetMod.class.getName());

    public static float newMonsterMulti = 1.0F;
    public boolean isDemo = true;

    public CvetMod() {
        BaseMod.subscribe(this);

        logger.info("addColor CVET_PINK");
        BaseMod.addColor(CVET_PINK,
                CvetPink, CvetPink, CvetPink, CvetPink, CvetPink, CvetPink, CvetPink,          //Background color, back color, frame color, frame outline color, description box color, glow color
                attackCard, skillCard, powerCard, energyOrb,                                   //attack background image, skill background image, power background image, energy orb image
                attackCardPortrait, skillCardPortrait, powerCardPortrait, energyOrbPortrait,   //as above, but for card inspect view
                miniManaSymbol);
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new cvetmod.CvetMod();
    }

    @Override
    public void receivePostInitialize() {
        initializeEvents();
        initializePotions();
        initializeMonsters();
    }

    public void initializeMonsters() {
//        String[] names = CardCrawlGame.languagePack.getUIString("cvetmod:RunHistoryMonsterNames").TEXT;
//        addMonster("Single ArcCommando", names[0], () -> new MonsterGroup(new AbstractMonster[] {new ArclightCommando(0.0F, 0.0F)}));
//        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo("Single ArcCommando", 2.0F * newMonsterMulti));
        // Add a name.
//        addMonster("Awaken", names[12], () -> new MonsterGroup(new Awaken_Monster(180.0F, 0.0F)));

    }

    private void initializeEvents() {
//        BaseMod.addEvent(new AddEventParams.Builder(TheCure.ID, TheCure.class).
//                eventType(EventUtils.EventType.NORMAL).
//                dungeonIDs("Exordium", "TheCity", "TheBeyond").
//                endsWithRewardsUI(false).
//                create());
    }

    private void initializePotions() {
//        BaseMod.addPotion(BottledCalcium.class, Color.GRAY, null, Color.DARK_GRAY, BottledCalcium.ID, cvetmod.patches.CvetEnum.RHINE_CLASS);
    }

    @Override
    public void receivePostDungeonInitialize() {}

    @Override
    public void receiveCustomModeMods(List<CustomMod> modList) {
    }

    @Override
    public int receiveOnPlayerLoseBlock(int cnt) {
        return cnt;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        Originium.originium.clear();
    }

    @Override
    public void receivePostBattle(final AbstractRoom p0) {}

    @Override
    public void receiveRelicGet(AbstractRelic r) {}

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AbstractCvetCard.SecondMagicNumber());
        CustomIconHelper.addCustomIcon(SecondEnergyIcon.get());

        ArrayList<AbstractCard> cards = new ArrayList<>();

        // Curse.
        cards.add(new Originium()); // 源石

        // Basic.
        cards.add(new CvetStrikeA()); // 打击·阿米娅
        cards.add(new CvetStrikeT()); // 打击·特蕾西娅
        cards.add(new CvetStrikeB()); // 打击·Both
        cards.add(new CvetDefendA()); // 防御·阿米娅
        cards.add(new CvetDefendT()); // 防御·特蕾西娅
        cards.add(new CvetDefendB()); // 防御·Both
        cards.add(new TacticalChant()); // 战术咏唱
        cards.add(new Analyse()); // 解构
        cards.add(new Inhibitor()); // 抑制剂

        // Common.
        cards.add(new Desperate()); // 绝望
        cards.add(new ExternalCombustionEngine()); // 外燃机
        cards.add(new EmergencyMedicineChest()); // 应急药箱

        // Uncommon.
        cards.add(new SpiritBurst()); // 精神爆发
        cards.add(new Saving()); // 拯救
        cards.add(new Struggle()); // 抗争

        // Rare.
        cards.add(new Terminate()); // 停止
        cards.add(new BreakRing()); // 碎戒
        cards.add(new Chimera()); // 奇美拉

        // Special.
        cards.add(new TheRealityOfEnd()); //终结的实相

        // Colourless

        for (AbstractCard c:cards) {
            BaseMod.addCard(c);
            UnlockTracker.seenPref.putInteger(c.cardID, 1);
        }
        UnlockTracker.seenPref.flush();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new cvetmod.characters.CivilightEterna(CardCrawlGame.playerName), charButton, charPortrait, CvetEnum.CVET_CLASS);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("cvetmod/strings/" + getLang() + "/keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        Map<String, Keyword> keywords = gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
            logger.info("Adding Keyword - " + v.NAMES[0]);
            BaseMod.addKeyword("cvetmod:", v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    @Override
    public void receiveEditRelics() {
        // starter.
//        BaseMod.addRelicToCustomPool(new TITStudentIdCard(), CVET_PINK);

        // common.

        // uncommon.
//        BaseMod.addRelic(new PittsAssortedFruits(), RelicType.SHARED);

        // rare.

        // boss.

        // event.

        // shop.
    }

    @Override
    public void receiveEditStrings() {
        String lang = getLang();
        String cardStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/cards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String characterStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/character.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);
        String powerStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/powers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String relicStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String eventStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        String uiStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/ui.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/potions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String scoreBonusesStrings = Gdx.files.internal("cvetmod/strings/" + lang + "/score_bonuses.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(ScoreBonusStrings.class, scoreBonusesStrings);
    }

    private String getLang() {
//        String lang = "eng";
//        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
//            lang = "zhs";
//        }
//        return lang;
        return "zhs";
    }

    public static void applyEnemyPowersOnly(DamageInfo info, AbstractCreature target) {
        info.isModified = false;
        float tmp = (float)info.base;

        for (AbstractPower p : target.powers) {
            tmp = p.atDamageReceive(tmp, info.type);
        }

        for (AbstractPower p : target.powers) {
            tmp = p.atDamageFinalReceive(tmp, info.type);
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }
        info.output = MathUtils.floor(tmp);
        if (info.base != info.output) {
            info.isModified = true;
        }
    }
}