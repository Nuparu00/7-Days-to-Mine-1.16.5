package nuparu.sevendaystomine.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.animation.Animation;
import nuparu.sevendaystomine.client.animation.Animations;
import nuparu.sevendaystomine.util.MathUtils;

import javax.annotation.Nullable;
import java.io.File;

@OnlyIn(Dist.CLIENT)
public class GuiAnimationDebug extends Screen implements IGuiEventListener {


    AnimationList animationList;
    boolean selection = false;

    DoneButton doneButton;
    Button pause;
    Button reset;
    Button repeat;
    Button override;
    Button save;
    Button select;
    AbstractSlider speedSlider;
    public GuiAnimationDebug() {
        super(new StringTextComponent("screen.animation_debug"));
    }

    @Override
    public void init() {
        this.animationList = new GuiAnimationDebug.AnimationList(this.minecraft);

        this.buttons.clear();
        pause = new Button(10, 20, 100, 20, new StringTextComponent("Pause/Unpause"), (button) -> {
            pause();
        });
        reset = new Button(115, 20, 100, 20, new StringTextComponent("Reset"), (button) -> {
            reset();
        });
        double speed = Animations.currentAnimation != null ? Animations.currentAnimation.getSpeed() : 1;
        speedSlider = new SpeedSlider(10, 45, 100, 20, new StringTextComponent("Speed: " + MathUtils.roundToNDecimal(speed, 2) + "x"), SpeedSlider.actualSpeedToSliderPosition(speed));

        repeat = new RepeatButton(10, 70, 50, 20, new StringTextComponent("Repeat"), (button) -> {
            repeat();
        });

        save = new Button(10, 95, 50, 20, new StringTextComponent("Export"), (button) -> {
            save();
        });

        select = new Button(width - 60, 20, 50, 20, new StringTextComponent("Select"), (button) -> {
            select();
        });

        doneButton = new DoneButton(width - 60, height - 40, 50, 20, new StringTextComponent("Select"), (button) -> {
            done();
        });

        override = new OverrideButton(10, 120, 50, 20, new StringTextComponent("Override"), (button) -> {
            override();
        });

        if (!selection) {
            this.addButton(pause);
            this.addButton(reset);
            this.addButton(repeat);
            this.addButton(speedSlider);
            this.addButton(save);
            this.addButton(select);
            this.addButton(override);
        } else {
            this.addButton(doneButton);
        }
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if(selection) {
            this.animationList.render(matrix, mouseX, mouseY, partialTicks);
        }
        super.render(matrix, mouseX, mouseY, partialTicks);
        long time = Animations.currentAnimation != null ? Animations.currentAnimation.getCurrentTime() : -1;
        long duration = Animations.currentAnimation != null ? Animations.currentAnimation.getDuration() : -1;
        String animationName = Animations.currentAnimation != null ? Animations.currentAnimation.name.toString() : "No Animation";
        font.draw(matrix, time + " / " + duration + "ms", 10, 10, 0xffffff);
        font.draw(matrix, animationName, width-font.width(animationName), 10, 0xffffff);

    }

    public void tick() {
        if(doneButton != null){
            doneButton.active = selection;
        }
    }


    @Override
    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double i) {
        return super.mouseScrolled(p_231043_1_, p_231043_3_, i);
    }

    public void pause() {
        if (Animations.currentAnimation != null) {
            Animations.currentAnimation.switchPause();
        }
    }

    public void reset() {
        if (Animations.currentAnimation != null) {
            Animations.currentAnimation.reset();
        }
    }

    public void repeat() {
        if (Animations.currentAnimation != null) {
            Animations.currentAnimation.setRepeat(!Animations.currentAnimation.isRepeat());
        }
    }
    public void save() {
        if (Animations.currentAnimation != null) {
            final File file = new File("./resources/animations/" + Animations.currentAnimation.name.getPath() + ".json");
            file.delete();
            Animations.currentAnimation.saveAsJson(file);
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Saved into " + file.getAbsolutePath()), Util.NIL_UUID);
        }
    }

    public void override() {
        Animations.override = !Animations.override;
    }

    public void select() {
        selection = true;
        this.children.add(animationList);
        removeButton(pause);
        removeButton(reset);
        removeButton(repeat);
        removeButton(speedSlider);
        removeButton(save);
        removeButton(select);
        removeButton(override);
        addButton(doneButton);

    }

    public void done() {
        selection = false;
        this.children.remove(animationList);
        Animations.currentAnimation=animationList.getSelected().animation;
        addButton(pause);
        addButton(reset);
        addButton(repeat);
        addButton(speedSlider);
        addButton(save);
        addButton(select);
        addButton(override);
        removeButton(doneButton);
    }

    public void removeButton(Widget button){
        this.buttons.remove(button);
        this.children.remove(button);
    }

    public static class SpeedSlider extends AbstractSlider {

        public SpeedSlider(int p_i232253_1_, int p_i232253_2_, int p_i232253_3_, int p_i232253_4_, ITextComponent p_i232253_5_, double p_i232253_6_) {
            super(p_i232253_1_, p_i232253_2_, p_i232253_3_, p_i232253_4_, p_i232253_5_, p_i232253_6_);
        }

        @Override
        protected void updateMessage() {
            this.setMessage(new StringTextComponent("Speed: " + MathUtils.roundToNDecimal(sliderPositionToActualSpeed(this.value),2)+ "x"));
        }

        @Override
        protected void applyValue() {
            if (Animations.currentAnimation != null) {
                Animations.currentAnimation.setSpeed(sliderPositionToActualSpeed(this.value));
            }
        }

        public static double actualSpeedToSliderPosition(double speed) {
            if (speed == 1) return 0.5;
            if (speed > 1) {
                return 0.5 + (speed - 1) / 8d;
            }
            return speed * 0.5;

        }

        public static double sliderPositionToActualSpeed(double position) {
            if (position == 0.5) return 1;
            if (position > 0.5) {
                return ((position - 0.5) * 8) + 1;
            }
            return position * 2;
        }
    }

    public static class RepeatButton extends Button{

        public RepeatButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
        }

        @Override
        public int getFGColor() {
            if (Animations.currentAnimation != null && Animations.currentAnimation.isRepeat()) {
                return 0x52ab2c;
            }
            return 0xffffff;
        }
    }

    public static class OverrideButton extends Button{

        public OverrideButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
        }

        @Override
        public int getFGColor() {
            if (Animations.override) {
                return 0x52ab2c;
            }
            return 0xffffff;
        }
    }
    public static class DoneButton extends Button{

        public DoneButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
        }
    }

    class AnimationList extends ExtendedList<GuiAnimationDebug.AnimationList.AnimationEntry> {
        public AnimationList(Minecraft p_i45519_2_) {
            super(p_i45519_2_, GuiAnimationDebug.this.width, GuiAnimationDebug.this.height, 32, GuiAnimationDebug.this.height - 65 + 4, 18);

            for(Animation animation : Animations.instance.getAnimations().values()) {
                GuiAnimationDebug.AnimationList.AnimationEntry languagescreen$list$languageentry = new GuiAnimationDebug.AnimationList.AnimationEntry(animation);
                this.addEntry(languagescreen$list$languageentry);
                if (Animations.currentAnimation != null && Animations.currentAnimation.name.toString().equals(animation.name.toString())) {
                    this.setSelected(languagescreen$list$languageentry);
                }
            }

            if (this.getSelected() != null) {
                this.centerScrollOn(this.getSelected());
            }

        }

        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 20;
        }

        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        public void setSelected(@Nullable GuiAnimationDebug.AnimationList.AnimationEntry p_241215_1_) {
            super.setSelected(p_241215_1_);
            if (p_241215_1_ != null) {
                NarratorChatListener.INSTANCE.sayNow((new TranslationTextComponent("narrator.select", p_241215_1_.animation)).getString());
            }

        }

        protected void renderBackground(MatrixStack p_230433_1_) {
            GuiAnimationDebug.this.renderBackground(p_230433_1_);
        }

        protected boolean isFocused() {
            return GuiAnimationDebug.this.getFocused() == this;
        }
        
        @OnlyIn(Dist.CLIENT)
        public class AnimationEntry extends ExtendedList.AbstractListEntry<AnimationEntry> {
            private final Animation animation;

            public AnimationEntry(Animation p_i50494_2_) {
                this.animation = p_i50494_2_;
            }

            public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
                String s = this.animation.name.toString();
                font.drawShadow(p_230432_1_, s, (float)(width / 2 - font.width(s) / 2), (float)(p_230432_3_ + 1), 16777215, true);
            }

            public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
                if (selection && p_231044_5_ == 0) {
                    this.select();
                    return true;
                } else {
                    return false;
                }
            }

            private void select() {
                AnimationList.this.setSelected(this);
            }
        }
    }
}
