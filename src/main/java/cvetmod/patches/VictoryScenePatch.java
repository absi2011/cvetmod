package cvetmod.patches;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;

@SpirePatch(clz = Cutscene.class, method = "<ctor>")
public class VictoryScenePatch {
    @SpirePostfixPatch
    public static void Postfix(Cutscene _inst, AbstractPlayer.PlayerClass chosenClass, @ByRef Texture[] ___bgImg, ArrayList<CutscenePanel> ___panels) {
        if (chosenClass == CvetEnum.CVET_CLASS) {
            String url = "cvetmod/images/ui/VictoryScene.png";
            ___bgImg[0] = ImageMaster.loadImage(url);
            ___panels.clear();
            ___panels.add(new CutscenePanel(url));
        }
    }
}
