package nuparu.sevendaystomine.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.entity.MinibikeEntity;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class ModelCar<T extends CarEntity> extends EntityModel<T> {
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer frontLeftDoor;
    private final ModelRenderer bone5;
    private final ModelRenderer frontRightDoor;
    private final ModelRenderer bone7;
    private final ModelRenderer backLeftDoor;
    private final ModelRenderer bone9;
    private final ModelRenderer backRightDoor;
    private final ModelRenderer bone11;
    private final ModelRenderer steering_wheel;
    private final ModelRenderer steering_wheel_axis;
    private final ModelRenderer bone13;
    private final ModelRenderer frontWheels;
    private final ModelRenderer rearWheels;
    private final ModelRenderer engine;
    private final ModelRenderer seats;
    private final ModelRenderer meters;

    private final TextModelRenderer fuelMeter;
    private final TextModelRenderer speedMeter;

    public ModelCar() {
        texWidth = 176;
        texHeight = 176;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 16.0F, -31.0F);
        setRotationAngle(bone, 0.2618F, 0.0F, 0.0F);
        bone.texOffs(77, 39).addBox(-14.0F, -3.8978F, -6.2235F, 28.0F, 2.0F, 11.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(13.0F, 13.5F, -14.0F);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        bone2.texOffs(8, 39).addBox(-1.01F, -18.6213F, -3.1213F, 2.0F, 19.0F, 2.0F, 0.0F, false);
        bone2.texOffs(0, 39).addBox(-26.99F, -18.6213F, -3.1213F, 2.0F, 19.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(13.0F, 15.9F, 29.0F);
        setRotationAngle(bone3, 0.6981F, 0.0F, 0.0F);
        bone3.texOffs(30, 147).addBox(-1.01F, -20.6207F, 0.4575F, 2.0F, 18.0F, 2.0F, 0.0F, false);
        bone3.texOffs(18, 127).addBox(-25.0F, -7.6207F, 0.4575F, 24.0F, 5.0F, 2.0F, 0.0F, false);
        bone3.texOffs(22, 147).addBox(-26.99F, -20.6207F, 0.4575F, 2.0F, 18.0F, 2.0F, 0.0F, false);

        frontLeftDoor = new ModelRenderer(this);
        frontLeftDoor.setPos(13.5F, 22.5F, -13.0F);
        frontLeftDoor.texOffs(0, 0).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 18.0F, 0.0F, false);
        frontLeftDoor.texOffs(67, 62).addBox(-0.5F, -21.5F, 16.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        frontLeftDoor.texOffs(0, 0).addBox(-0.5F, -22.5F, 11.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -16.5F, 8.5F);
        frontLeftDoor.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(24, 0).addBox(-0.5F, -6.1213F, -2.6213F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        frontRightDoor = new ModelRenderer(this);
        frontRightDoor.setPos(-13.5F, 22.5F, -13.0F);
        frontRightDoor.texOffs(110, 110).addBox(-0.5F, -11.5F, 0.0F, 1.0F, 9.0F, 17.0F, 0.0F, false);
        frontRightDoor.texOffs(4, 62).addBox(-0.5F, -21.5F, 16.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        frontRightDoor.texOffs(0, 8).addBox(-0.5F, -22.5F, 11.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, -16.5F, 8.5F);
        frontRightDoor.addChild(bone7);
        setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
        bone7.texOffs(20, 0).addBox(-0.5F, -6.1213F, -2.6213F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        backLeftDoor = new ModelRenderer(this);
        backLeftDoor.setPos(13.5F, 22.5F, 2.0F);
        backLeftDoor.texOffs(55, 127).addBox(-0.5F, -11.5F, 3.0F, 1.0F, 9.0F, 15.0F, 0.0F, false);
        backLeftDoor.texOffs(0, 62).addBox(-0.5F, -21.5F, 2.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        backLeftDoor.texOffs(22, 134).addBox(-0.5F, -22.5F, 3.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, -16.5F, 11.5F);
        backLeftDoor.addChild(bone9);
        setRotationAngle(bone9, 0.6981F, 0.0F, 0.0F);
        bone9.texOffs(0, 121).addBox(-0.52F, -3.1852F, 5.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        backRightDoor = new ModelRenderer(this);
        backRightDoor.setPos(-13.5F, 22.5F, 2.0F);
        backRightDoor.texOffs(0, 121).addBox(-0.5F, -11.5F, 2.0F, 1.0F, 9.0F, 16.0F, 0.0F, false);
        backRightDoor.texOffs(31, 27).addBox(-0.5F, -21.5F, 2.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        backRightDoor.texOffs(133, 133).addBox(-0.5F, -22.5F, 2.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, -16.5F, 7.5F);
        backRightDoor.addChild(bone11);
        setRotationAngle(bone11, 0.6981F, 0.0F, 0.0F);
        bone11.texOffs(16, 39).addBox(-0.48F, -0.1852F, 8.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        steering_wheel = new ModelRenderer(this);
        steering_wheel.setPos(7.0F, 13.0425F, -11.5293F);
        setRotationAngle(steering_wheel, 0.6109F, 0.0F, 0.0F);


        steering_wheel_axis = new ModelRenderer(this);
        steering_wheel_axis.setPos(0.0F, 0.0F, 0.0F);
        steering_wheel.addChild(steering_wheel_axis);
        steering_wheel_axis.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        steering_wheel_axis.texOffs(8, 8).addBox(-1.5F, -1.5F, 0.25F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, 24.0F, 0.0F);
        bone13.texOffs(0, 39).addBox(-14.0F, -26.0F, -3.5F, 28.0F, 2.0F, 21.0F, 0.0F, false);
        bone13.texOffs(72, 82).addBox(-14.0F, -12.0F, 21.0F, 28.0F, 3.0F, 10.0F, 0.0F, false);
        bone13.texOffs(67, 67).addBox(-14.0F, -13.0F, -28.0F, 28.0F, 4.0F, 11.0F, 0.0F, false);
        bone13.texOffs(94, 0).addBox(-11.0F, -9.0F, 21.0F, 22.0F, 6.0F, 10.0F, 0.0F, false);
        bone13.texOffs(94, 16).addBox(-11.0F, -9.0F, -27.0F, 22.0F, 6.0F, 10.0F, 0.0F, false);
        bone13.texOffs(0, 95).addBox(-14.0F, -15.0F, 20.0F, 28.0F, 3.0F, 6.0F, 0.0F, false);
        bone13.texOffs(0, 0).addBox(-14.0F, -4.0F, -17.0F, 28.0F, 1.0F, 38.0F, 0.0F, false);
        bone13.texOffs(0, 62).addBox(-14.0F, -8.0F, -38.0F, 28.0F, 5.0F, 11.0F, 0.0F, false);
        bone13.texOffs(0, 27).addBox(-6.0F, -8.0F, -39.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        bone13.texOffs(28, 0).addBox(-7.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(11, 32).addBox(-13.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 29).addBox(8.0F, -9.0F, -39.0F, 5.0F, 3.0F, 1.0F, 0.0F, false);
        bone13.texOffs(23, 27).addBox(6.0F, -11.0F, -40.0F, 1.0F, 7.0F, 3.0F, 0.0F, false);
        bone13.texOffs(0, 104).addBox(-14.0F, -11.0F, -32.0F, 28.0F, 3.0F, 5.0F, 0.0F, false);
        bone13.texOffs(61, 108).addBox(-14.0F, -10.0F, -37.0F, 28.0F, 2.0F, 5.0F, 0.0F, false);
        bone13.texOffs(94, 32).addBox(-14.0F, -10.0F, -38.0F, 28.0F, 2.0F, 1.0F, 0.0F, false);
        bone13.texOffs(0, 78).addBox(-14.0F, -12.0F, 31.0F, 28.0F, 9.0F, 8.0F, 0.0F, false);
        bone13.texOffs(68, 95).addBox(-14.0F, -13.0F, -17.0F, 28.0F, 9.0F, 4.0F, 0.0F, false);
        bone13.texOffs(0, 112).addBox(-14.0F, -12.0F, 20.0F, 28.0F, 8.0F, 1.0F, 0.0F, false);

        frontWheels = new ModelRenderer(this);
        frontWheels.setPos(0.0F, 20.0F, -22.0F);
        frontWheels.texOffs(0, 146).addBox(11.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, 0.0F, false);
        frontWheels.texOffs(87, 140).addBox(-14.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, 0.0F, false);

        rearWheels = new ModelRenderer(this);
        rearWheels.setPos(0.0F, 20.0F, 26.0F);
        rearWheels.texOffs(144, 32).addBox(11.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, 0.0F, false);
        rearWheels.texOffs(40, 143).addBox(-14.0F, -4.0F, -4.0F, 3.0F, 8.0F, 8.0F, 0.0F, false);

        engine = new ModelRenderer(this);
        engine.setPos(0.0F, 24.0F, 0.0F);
        engine.texOffs(129, 111).addBox(-7.0F, -4.0F, 30.0F, 2.0F, 2.0F, 12.0F, 0.0F, false);

        seats = new ModelRenderer(this);
        seats.setPos(0.0F, 24.0F, 0.0F);
        seats.texOffs(129, 99).addBox(-12.0F, -7.0F, -7.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        seats.texOffs(72, 127).addBox(2.0F, -7.0F, -7.0F, 10.0F, 3.0F, 9.0F, 0.0F, false);
        seats.texOffs(134, 65).addBox(2.0F, -16.0F, 0.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        seats.texOffs(107, 136).addBox(-12.0F, -16.0F, 0.0F, 10.0F, 9.0F, 3.0F, 0.0F, false);
        seats.texOffs(89, 53).addBox(-12.0F, -7.0F, 6.0F, 24.0F, 3.0F, 9.0F, 0.0F, false);
        seats.texOffs(58, 115).addBox(-12.0F, -16.0F, 13.0F, 24.0F, 9.0F, 3.0F, 0.0F, false);


        meters = new ModelRenderer(this);
        meters.setPos(-5, 12, -13);
        fuelMeter = new TextModelRenderer(0,0,-0.01,180,180,0);
        fuelMeter.scale = 0.0075f;
        speedMeter = new TextModelRenderer(0,-0.06,-0.01,180,180,0);
        speedMeter.scale = 0.0075f;
        meters.addChild(fuelMeter);
        meters.addChild(speedMeter);

    }

    @Override
    public void setupAnim(CarEntity car, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float partialTicks) {

        //parachute.visible = (!entity.isOnGround() && !entity.getLanded());

        float rotation = car.wheelAnglePrev + (car.wheelAngle - car.wheelAnglePrev) * partialTicks;
        if (rotation != 0) {
            setRotationAngle(frontWheels, rotation, 0, 0);
            setRotationAngle(rearWheels, rotation, 0, 0);
        }
        float frontRotation = Utils.lerp(car.getFrontRotationPrev(), car.getFrontRotation(), partialTicks) * 0.0174533f * 90.5f;
        //-Utils.lerp(car.getTurningPrev(),car.getTurning(),partialTicks)*0.0174533f*1.5f,0)

        double speed = car.kmh;
        //If the speed is below 1.0, we are interested only in the fist 2 decimal digits
        if(speed < 1){
            speed = MathUtils.roundToNDecimal(speed,2);
        }
        //Otherwise we wan to first 3 significant digits
        else{
            speed = MathUtils.roundToSignificantFigures(speed,3);
        }


        fuelMeter.setText((int)(Math.ceil((car.getFuel()/ MinibikeEntity.MAX_FUEL)*100f))+"%");
        speedMeter.setText(speed+"");
        setRotationAngle(steering_wheel_axis, 0,0, -frontRotation);

        frontWheels.visible = car.getWheelsFront();
        rearWheels.visible = car.getWheelsBack();
        rearWheels.visible = car.getWheelsBack();
        rearWheels.visible = car.getWheelsBack();
        engine.visible = car.getEngine();
        seats.visible = car.getSeat();
        steering_wheel.visible = car.getHandles();


        /*Handle1.visible = car.getHandles();
        Handle2.visible = car.getHandles();
        Frontwheel.visible = car.getWheels();
        Backwheel.visible = car.getWheels();
        Battery.visible = car.getBattery();
        Seat.visible = car.getSeat();
        Engine1.visible = car.getEngine();
        Engine2.visible = car.getEngine();
        Engine3.visible = car.getEngine();
        Chest.visible = car.getChest();*/
    }

    private void setRotationAngle(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
        bone2.render(matrixStack, buffer, packedLight, packedOverlay);
        bone3.render(matrixStack, buffer, packedLight, packedOverlay);
        frontLeftDoor.render(matrixStack, buffer, packedLight, packedOverlay);
        frontRightDoor.render(matrixStack, buffer, packedLight, packedOverlay);
        backLeftDoor.render(matrixStack, buffer, packedLight, packedOverlay);
        backRightDoor.render(matrixStack, buffer, packedLight, packedOverlay);
        steering_wheel.render(matrixStack, buffer, packedLight, packedOverlay);
        bone13.render(matrixStack, buffer, packedLight, packedOverlay);
        frontWheels.render(matrixStack, buffer, packedLight, packedOverlay);
        rearWheels.render(matrixStack, buffer, packedLight, packedOverlay);
        engine.render(matrixStack, buffer, packedLight, packedOverlay);
        seats.render(matrixStack, buffer, packedLight, packedOverlay);
        meters.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
