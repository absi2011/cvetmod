package cvetmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.EnlightenmentAction;
import com.megacrit.cardcrawl.actions.unique.ForethoughtAction;
import com.megacrit.cardcrawl.actions.unique.MadnessAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import cvetmod.cards.AbstractCvetCard;

import java.lang.reflect.Field;

public class SecondCostPatch {
    // 这里Patch的是各种 耗能变为1/0的内容
    // EnlightenmentAction : 耗能变为1
    /* TODO: 结果：CE了，搞不定，noname来救救我！
    @SpirePatch(clz = EnlightenmentAction.class, method = "update")
    public static class EnlightenmentActionPatch {
        @SpireInsertPatch(rloc = 24, localvars = {"c"})
        public static void Insert(EnlightenmentAction __inst, AbstractCard c) {
            try {
                Field field = EnlightenmentAction.class.getDeclaredField("forCombat");
                field.setAccessible(true);
                boolean forCombat = (field.getBoolean(__inst));
                if (c instanceof AbstractCvetCard) {
                    if (((AbstractCvetCard) c).getSecondCost() > 1) {
                        ((AbstractCvetCard) c).setSecondCostThisTurn(1);
                    }
                    if ((forCombat) && ((AbstractCvetCard) c).secondCost > 1) {
                        ((AbstractCvetCard) c).upgradeBaseSecondCost(1);
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    */
    // ForethoughtAction : 判断耗能>0
    // MadnessAction : 判断耗能>0，耗能变为0（建议重写）
    // RandomizeHandCostAction : 建议重写
    // SetupAction : 判断耗能>0
    // Chrysalis : 判断耗能>0，耗能变为0（建议重写）
    // Metamorphosis : 判断耗能>0，耗能变为0（建议重写）
    // ConfusionPower : 可以用PostPatch搞定
    // Enchiridion : 可以直接重写
    // MummifiedHand : 判断耗能>0
    // Necronomicon : 费用>=2，建议判断为任意费用>=2
    // WristBlade : 理论见不到，也很好重写
}
