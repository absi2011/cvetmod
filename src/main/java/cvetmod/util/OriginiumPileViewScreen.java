package cvetmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import cvetmod.CvetMod;
import cvetmod.cards.special.Originium;

public class OriginiumPileViewScreen implements ScrollBarListener {
    private final CardGroup originiumPileCopy;
    public boolean isHovered;
    private boolean grabbedScreen;
    private float grabStartY;
    private float currentDiffY;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private final float scrollLowerBound;
    private float scrollUpperBound;
    private AbstractCard hoveredCard;
    private int prevDeckSize;
    private static final float SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
    private final ScrollBar scrollBar;
    private AbstractCard controllerCard;

    @SpireEnum
    public static AbstractDungeon.CurrentScreen ORIGINIUM_VIEW;

    public OriginiumPileViewScreen() {
        this.originiumPileCopy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        this.isHovered = false;
        this.grabbedScreen = false;
        this.grabStartY = 0.0F;
        this.currentDiffY = 0.0F;
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
        this.prevDeckSize = 0;
        this.controllerCard = null;
        drawStartX = Settings.WIDTH;
        drawStartX -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        scrollBar = new ScrollBar(this);
        scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    public void update() {
        boolean isDraggingScrollBar = false;
        if (shouldShowScrollBar()) {
            isDraggingScrollBar = scrollBar.update();
        }

        if (!isDraggingScrollBar) {
            updateScrolling();
        }

        if (!originiumPileCopy.group.isEmpty()) {
            updateControllerInput();
        }

        if (Settings.isControllerMode && controllerCard != null && !CardCrawlGame.isPopupOpen && !CInputHelper.isTopPanelActive()) {
            if (Gdx.input.getY() > Settings.HEIGHT * 0.7F) {
                currentDiffY += Settings.SCROLL_SPEED;
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.3F) {
                currentDiffY -= Settings.SCROLL_SPEED;
            }
        }

        updatePositions();
        if (Settings.isControllerMode && controllerCard != null && !CInputHelper.isTopPanelActive()) {
            CInputHelper.setCursor(controllerCard.hb);
        }

    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !CInputHelper.isTopPanelActive()) {
            boolean anyHovered = false;
            int index = 0;

            for (AbstractCard c : this.originiumPileCopy.group) {
                if (c.hb.hovered) {
                    anyHovered = true;
                    break;
                }
                index++;
            }

            if (!anyHovered) {
                CInputHelper.setCursor((originiumPileCopy.group.get(0)).hb);
                controllerCard = originiumPileCopy.group.get(0);
            } else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && originiumPileCopy.size() > 5) {
                index -= 5;
                if (index < 0) {
                    int wrap = originiumPileCopy.size() / 5;
                    index += wrap * 5;
                    if (index + 5 < originiumPileCopy.size()) {
                        index += 5;
                    }
                }

                CInputHelper.setCursor((originiumPileCopy.group.get(index)).hb);
                controllerCard = originiumPileCopy.group.get(index);
            } else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && originiumPileCopy.size() > 5) {
                if (index < originiumPileCopy.size() - 5) {
                    index += 5;
                } else {
                    index %= 5;
                }

                CInputHelper.setCursor((originiumPileCopy.group.get(index)).hb);
                this.controllerCard = originiumPileCopy.group.get(index);
            } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    if (index % 5 < 4) {
                        index++;
                        if (index > originiumPileCopy.size() - 1) {
                            index -= originiumPileCopy.size() % 5;
                        }
                    } else {
                        index -= 4;
                    }

                    CInputHelper.setCursor((originiumPileCopy.group.get(index)).hb);
                    controllerCard = originiumPileCopy.group.get(index);
                }
            } else {
                if (index % 5 > 0) {
                    index--;
                } else {
                    index += 4;
                    if (index > originiumPileCopy.size() - 1) {
                        index = originiumPileCopy.size() - 1;
                    }
                }

                CInputHelper.setCursor((originiumPileCopy.group.get(index)).hb);
                controllerCard = originiumPileCopy.group.get(index);
            }

        }
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!grabbedScreen) {
            if (InputHelper.scrolledDown) {
                currentDiffY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                currentDiffY -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                grabbedScreen = true;
                grabStartY = y - currentDiffY;
            }
        } else if (InputHelper.isMouseDown) {
            currentDiffY = y - grabStartY;
        } else {
            grabbedScreen = false;
        }

        if (prevDeckSize != originiumPileCopy.size()) {
            calculateScrollBounds();
        }

        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        if (originiumPileCopy.size() > 10) {
            int scrollTmp = originiumPileCopy.size() / 5 - 2;
            if (originiumPileCopy.size() % 5 != 0) {
                ++scrollTmp;
            }

            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * padY;
        } else {
            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        prevDeckSize = originiumPileCopy.size();
    }

    private void resetScrolling() {
        if (currentDiffY < scrollLowerBound) {
            currentDiffY = MathHelper.scrollSnapLerpSpeed(currentDiffY, scrollLowerBound);
        } else if (currentDiffY > scrollUpperBound) {
            currentDiffY = MathHelper.scrollSnapLerpSpeed(currentDiffY, scrollUpperBound);
        }

    }

    private void updatePositions() {
        this.hoveredCard = null;
        int lineNum = 0;
        int mod = 0;
        for (AbstractCard card : originiumPileCopy.group) {
            card.target_x = drawStartX + mod * padX;
            card.target_y = drawStartY + currentDiffY - lineNum * padY;
            card.update();
            if (AbstractDungeon.topPanel.potionUi.isHidden) {
                card.updateHoverLogic();
                if (card.hb.hovered) {
                    this.hoveredCard = card;
                }
            }
            mod++;
            if (mod == 5) {
                mod = 0;
                lineNum++;
            }
        }

    }

    public void reopen() {
        AbstractDungeon.overlayMenu.cancelButton.show(CvetMod.ORIGINIUM_VIEW_TEXT[1]);
    }

    public void open() {
        CardCrawlGame.sound.play("DECK_OPEN");
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(CvetMod.ORIGINIUM_VIEW_TEXT[1]);
        currentDiffY = 0.0F;
        grabStartY = 0.0F;
        grabbedScreen = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = ORIGINIUM_VIEW;
        originiumPileCopy.clear();

        for (AbstractCard c : Originium.originiumPile.group) {
            AbstractCard toAdd = c.makeStatEquivalentCopy();
            toAdd.setAngle(0.0F, true);
            toAdd.targetDrawScale = 0.75F;
            toAdd.drawScale = 0.75F;
            toAdd.lighten(true);
            originiumPileCopy.addToBottom(toAdd);
        }

        if (!AbstractDungeon.player.hasRelic("Frozen Eye")) {
            originiumPileCopy.sortAlphabetically(true);
            originiumPileCopy.sortByRarityPlusStatusCardType(true);
        }

        hideCards();
        if (originiumPileCopy.group.size() <= 5) {
            drawStartY = Settings.HEIGHT * 0.5F;
        } else {
            drawStartY = Settings.HEIGHT * 0.66F;
        }

        calculateScrollBounds();
    }

    private void hideCards() {
        int lineNum = 0;
        int mod = 0;
        for (AbstractCard card : originiumPileCopy.group) {
            card.current_x = drawStartX + mod * padX;
            card.current_y = drawStartY + currentDiffY - lineNum * padY - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            card.targetDrawScale = 0.75F;
            card.drawScale = 0.75F;

            mod++;
            if (mod == 5) {
                mod = 0;
                lineNum++;
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (shouldShowScrollBar()) {
            scrollBar.render(sb);
        }

        if (hoveredCard == null) {
            originiumPileCopy.render(sb);
        } else {
            originiumPileCopy.renderExceptOneCard(sb, hoveredCard);
            hoveredCard.renderHoverShadow(sb);
            hoveredCard.render(sb);
            hoveredCard.renderCardTip(sb);
        }

        FontHelper.renderDeckViewTip(sb, CvetMod.ORIGINIUM_VIEW_TEXT[0], 96.0F * Settings.scale, Settings.CREAM_COLOR);
    }

    public void scrolledUsingBar(float newPercent) {
        currentDiffY = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, newPercent);
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, currentDiffY);
        scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return scrollUpperBound > SCROLL_BAR_THRESHOLD;
    }
}
