package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import nuparu.sevendaystomine.entity.MinibikeEntity;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class ModelMinibike<T extends MinibikeEntity> extends EntityModel<T> {
    private final ModelRenderer Seat;
    private final ModelRenderer Battery;
    private final ModelRenderer Chassis12;
    private final ModelRenderer Chassis11;
    private final ModelRenderer Front;
    private final ModelRenderer Frontwheel;
    private final ModelRenderer Chassis2;
    private final ModelRenderer Chassis1;
    private final ModelRenderer Handle1;
    private final ModelRenderer Handle2;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer Chassis10;
    private final ModelRenderer Chassis4;
    private final ModelRenderer Chassis9;
    private final ModelRenderer Chassis5;
    private final ModelRenderer Chassis8;
    private final ModelRenderer Chassis6;
    private final ModelRenderer Chassis7;
    private final ModelRenderer Engine1;
    private final ModelRenderer Engine2;
    private final ModelRenderer Engine3;
    private final ModelRenderer Backwheel;
    private final ModelRenderer Chest;

    private final TextModelRenderer fuelMeter;
    private final TextModelRenderer speedMeter;

    public ModelMinibike() {
        fuelMeter = new TextModelRenderer(0.012,-0.10,-0.0001);
        speedMeter = new TextModelRenderer(0.012,-0.06,-0.0001);

        texWidth = 64;
        texHeight = 64;

        Seat = new ModelRenderer(this);
        Seat.setPos(-2.0F, 11.0F, -2.0F);
        Seat.texOffs(15, 45).addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 9.0F, 0.0F, true);

        Battery = new ModelRenderer(this);
        Battery.setPos(-1.5F, 14.0F, -6.0F);
        Battery.texOffs(0, 42).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 4.0F, 0.0F, true);

        Chassis12 = new ModelRenderer(this);
        Chassis12.setPos(-2.5F, 21.0F, 6.0F);
        Chassis12.texOffs(21, 0).addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, 0.0F, true);

        Chassis11 = new ModelRenderer(this);
        Chassis11.setPos(0.5F, 14.0F, -8.0F);
        setRotationAngle(Chassis11, 0.6283F, -0.5236F, 0.0F);
        Chassis11.texOffs(0, 20).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F, 0.0F, true);

        Front = new ModelRenderer(this);
        Front.setPos(0.0F, 14.0F, -7.0F);
        Front.texOffs(0, 34).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 3.0F, 3.0F, 0.0F, true);

        Frontwheel = new ModelRenderer(this);
        Frontwheel.setPos(0.0F, 7.5F, -2.5F);
        Front.addChild(Frontwheel);
        Frontwheel.texOffs(37, 0).addBox(-1.5F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, 0.0F, true);

        Chassis2 = new ModelRenderer(this);
        Chassis2.setPos(-2.5F, -8.0F, -0.5F);
        Front.addChild(Chassis2);
        setRotationAngle(Chassis2, -0.1745F, 0.0F, 0.0F);
        Chassis2.texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 1.0F, 0.0F, true);

        Chassis1 = new ModelRenderer(this);
        Chassis1.setPos(1.5F, -8.0F, -0.5F);
        Front.addChild(Chassis1);
        setRotationAngle(Chassis1, -0.1745F, 0.0F, 0.0F);
        Chassis1.texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 1.0F, 0.0F, true);

        Handle1 = new ModelRenderer(this);
        Handle1.setPos(2.5F, -8.0F, -0.5F);
        Front.addChild(Handle1);
        setRotationAngle(Handle1, -0.1745F, 0.0F, 0.0F);
        Handle1.texOffs(10, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        Handle2 = new ModelRenderer(this);
        Handle2.setPos(-6.5F, -8.0F, -0.5F);
        Front.addChild(Handle2);
        setRotationAngle(Handle2, -0.1745F, 0.0F, 0.0F);
        Handle2.texOffs(10, 0).addBox(0.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 1.0F);
        Handle2.addChild(bone);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(4.0F, 0.0F, 0.0F);
        bone.addChild(cube_r1);
        bone.addChild(fuelMeter);
        bone.addChild(speedMeter);
        setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 61).addBox(-0.5F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        Chassis10 = new ModelRenderer(this);
        Chassis10.setPos(-0.5F, 14.0F, -8.0F);
        setRotationAngle(Chassis10, 0.6283F, 0.5236F, 0.0F);
        Chassis10.texOffs(0, 20).addBox(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 1.0F, 0.0F, true);

        Chassis4 = new ModelRenderer(this);
        Chassis4.setPos(-0.5F, 13.0F, -8.0F);
        setRotationAngle(Chassis4, 0.0F, 0.1745F, 0.0F);
        Chassis4.texOffs(19, 2).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 16.0F, 0.0F, true);

        Chassis9 = new ModelRenderer(this);
        Chassis9.setPos(-3.5F, 21.0F, -3.0F);
        Chassis9.texOffs(5, 3).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, 0.0F, true);

        Chassis5 = new ModelRenderer(this);
        Chassis5.setPos(-0.5F, 13.0F, -8.0F);
        setRotationAngle(Chassis5, 0.0F, -0.1745F, 0.0F);
        Chassis5.texOffs(19, 2).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 16.0F, 0.0F, true);

        Chassis8 = new ModelRenderer(this);
        Chassis8.setPos(2.5333F, 21.0F, -3.0F);
        Chassis8.texOffs(5, 3).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, 0.0F, true);

        Chassis6 = new ModelRenderer(this);
        Chassis6.setPos(-0.5F, 15.5F, -8.0F);
        setRotationAngle(Chassis6, 0.1571F, 0.1745F, 0.0F);
        Chassis6.texOffs(5, 20).addBox(0.0F, 0.0F, 15.0F, 1.0F, 9.0F, 1.0F, 0.0F, true);

        Chassis7 = new ModelRenderer(this);
        Chassis7.setPos(-0.5F, 15.5F, -8.0F);
        setRotationAngle(Chassis7, 0.1571F, -0.1745F, 0.0F);
        Chassis7.texOffs(5, 20).addBox(0.0F, 0.0F, 15.0F, 1.0F, 9.0F, 1.0F, 0.0F, true);

        Engine1 = new ModelRenderer(this);
        Engine1.setPos(2.0F, 17.0F, -3.0F);
        Engine1.texOffs(10, 17).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F, 0.0F, true);

        Engine2 = new ModelRenderer(this);
        Engine2.setPos(-2.0F, 18.0F, -3.0F);
        Engine2.texOffs(17, 22).addBox(0.0F, 0.0F, 0.0F, 4.0F, 3.0F, 6.0F, 0.0F, true);

        Engine3 = new ModelRenderer(this);
        Engine3.setPos(-3.0F, 18.4F, 0.0F);
        Engine3.texOffs(10, 30).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);

        Backwheel = new ModelRenderer(this);
        Backwheel.setPos(0.0F, 21.5F, 6.5F);
        Backwheel.texOffs(37, 0).addBox(-1.5F, -2.5F, -2.5F, 3.0F, 5.0F, 5.0F, 0.0F, true);

        Chest = new ModelRenderer(this);
        Chest.setPos(1.0F, 13.0F, -3.0F);
        Chest.texOffs(16, 32).addBox(-4.0F, -6.0F, 7.0F, 6.0F, 6.0F, 6.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(MinibikeEntity minibike, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float partialTicks) {

        //parachute.visible = (!entity.isOnGround() && !entity.getLanded());

        float rotation = minibike.wheelAnglePrev + (minibike.wheelAngle - minibike.wheelAnglePrev) * partialTicks;
        if (rotation != 0) {
            setRotationAngle(Frontwheel, rotation, 0, 0);
            setRotationAngle(Backwheel, rotation, 0, 0);
        }
        float frontRotation = Utils.lerp(minibike.getFrontRotationPrev(), minibike.getFrontRotation(), partialTicks) * 0.0174533f * 90.5f;
        //-Utils.lerp(minibike.getTurningPrev(),minibike.getTurning(),partialTicks)*0.0174533f*1.5f,0)

        fuelMeter.setText((int)(Math.ceil((minibike.getFuel()/MinibikeEntity.MAX_FUEL)*100f))+"%");
        double speed = minibike.kmh;
        //If the speed is below 1.0, we are interested only in the fist 2 decimal digits
        if(speed < 1){
            speed = MathUtils.roundToNDecimal(speed,2);
        }
        //Otherwise we wan to first 3 significant digits
        else{
            speed = MathUtils.roundToSignificantFigures(speed,3);
        }
        speedMeter.setText(speed+"");
        setRotationAngle(Front, 0f, frontRotation, 0);


        Handle1.visible = minibike.getHandles();
        Handle2.visible = minibike.getHandles();
        Frontwheel.visible = minibike.getWheels();
        Backwheel.visible = minibike.getWheels();
        Battery.visible = minibike.getBattery();
        Seat.visible = minibike.getSeat();
        Engine1.visible = minibike.getEngine();
        Engine2.visible = minibike.getEngine();
        Engine3.visible = minibike.getEngine();
        Chest.visible = minibike.getChest();
    }

    private void setRotationAngle(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {

        Seat.render(matrixStack, buffer, packedLight, packedOverlay);
        Battery.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis12.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis11.render(matrixStack, buffer, packedLight, packedOverlay);
        Front.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis10.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis4.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis9.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis5.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis8.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis6.render(matrixStack, buffer, packedLight, packedOverlay);
        Chassis7.render(matrixStack, buffer, packedLight, packedOverlay);
        Engine1.render(matrixStack, buffer, packedLight, packedOverlay);
        Engine2.render(matrixStack, buffer, packedLight, packedOverlay);
        Engine3.render(matrixStack, buffer, packedLight, packedOverlay);
        Backwheel.render(matrixStack, buffer, packedLight, packedOverlay);
        Chest.render(matrixStack, buffer, packedLight, packedOverlay);



    }
}
