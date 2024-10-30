package cvetmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;

import java.util.HashMap;

public class MusicPatch {
    @SpirePatch(clz = SoundMaster.class, method = "<ctor>")
    public static class SoundTrackPatch {
        @SpireInsertPatch(rloc = 3, localvars = {"map"})
        public static void Insert(SoundMaster __instance, HashMap<String, Sfx> map) {
//            map.put("AS_YOU_WISH", new Sfx("audio/sound/p_skill_asyouwish.mp3", false));
        }
    }
}
