package nuparu.sevendaystomine.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.client.animation.Animation;
import nuparu.sevendaystomine.client.animation.Animations;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.item.ItemWire;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.PlayerUtils;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RenderEventHandler {

    public static long shotAnimationTimer = 0;
    public static int mainMuzzleFlash = 0;
    public static double mainMuzzleFlashAngle = 0;
    public static int sideMuzzleFlash = 0;
    public static double sideMuzzleFlashAngle = 0;
    private EnumHandPos handPos = EnumHandPos.NONE;
    private boolean aiming = false;
    private boolean scoping = false;
    private ItemGun gun = null;

    public static void renderArmWithItem(Minecraft minecraft, AbstractClientPlayerEntity player, float p_228405_2_, float p_228405_3_, Hand hand, float p_228405_5_, ItemStack p_228405_6_, float p_228405_7_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        boolean flag = hand == Hand.MAIN_HAND;
        HandSide handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        matrixStack.pushPose();
        if (!p_228405_6_.isEmpty()) {
            boolean flag3 = handside == HandSide.RIGHT;

            minecraft.getItemInHandRenderer().renderItem(player, p_228405_6_, flag3 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, matrixStack, buffer, light);
        }

        matrixStack.popPose();
    }

    public static void renderConnection(ElectricConnection connection, World world, Matrix4f matrix, IRenderTypeBuffer.Impl buffer, boolean hightlight, boolean reversed) {
        renderConnection(connection.getRenderFrom(world), connection.getRenderTo(world), world, matrix, buffer, hightlight, reversed);
    }

    public static void renderConnection(Vector3d start, Vector3d end, World world, Matrix4f matrix, IRenderTypeBuffer.Impl buffer, boolean hightlight, boolean reversed) {
        int distance = (int) Math.ceil(start.distanceToSqr(end));
        for (int i = 0; i <= 64; i++) {
            IVertexBuilder builder = buffer.getBuffer(RenderType.lines());
            Vector3d pointA = getLinePos(start, end, distance, i, 64, world);
            Vector3d pointB = getLinePos(start, end, distance, i + 1, 64, world);
            long secs = System.currentTimeMillis() / 50;
            int u = (int) (secs % 64);

            float alpha = 1f;

            int reversedIndex = reversed ? i : 64 - i;
            int powerIndex = u / 2;


            if (hightlight && (reversedIndex - powerIndex) % 8 == 0) {
                alpha = 5;
            }

            builder.vertex(matrix, (float) pointA.x, (float) pointA.y, (float) pointA.z).color(0.1f * alpha, 0.1f * alpha, 0.1f * alpha, 1f).endVertex();
            builder.vertex(matrix, (float) pointB.x, (float) pointB.y, (float) pointB.z).color(0.1f * alpha, 0.1f * alpha, 0.1f * alpha, 1f).endVertex();
            buffer.endBatch(RenderType.lines());
        }

    }

    public static Vector3d getLinePos(Vector3d start, Vector3d end, int dst, int index, int res, World world) {
        Vector3d vec = MathUtils.lerp(start, end, (float) index / res);
        double deltaY = Math.sin(((float) index / res) * Math.PI);
        double distanceSqrt = Math.sqrt(dst);
        if (distanceSqrt < ModConstants.MAXIMAL_LENGTH) {
            deltaY /= (Math.abs(distanceSqrt - ModConstants.MAXIMAL_LENGTH) / 2d);
        }
        BlockPos pos = new BlockPos(vec.x, vec.y - deltaY, vec.z);
        BlockState state = world.getBlockState(pos);
        if (state.isFaceSturdy(world, pos, Direction.UP)) {
            deltaY = vec.y - pos.above().getY() - 0.00625d;
        }
        return vec.subtract(0, deltaY, 0);
    }

    public static void renderConnectionToHand(Vector3d start, PlayerEntity player, World world, MatrixStack matrixStack, IRenderTypeBuffer.Impl buffer, float partialTicks, boolean hightlight, boolean reversed) {

        Minecraft mc = Minecraft.getInstance();

        int i = player.getMainArm() == HandSide.RIGHT ? 1 : -1;
        ItemStack itemstack = player.getMainHandItem();

        float f = player.getAttackAnim(partialTicks);
        float f1 = MathHelper.sin(MathHelper.sqrt(f) * (float) Math.PI);
        float f2 = MathHelper.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
        double d0 = MathHelper.sin(f2);
        double d1 = MathHelper.cos(f2);
        double d2 = (double) i * 0.35D;
        double d3 = 0.8D;
        double d4;
        double d5;
        double d6;
        float f3;
        if ((mc.getEntityRenderDispatcher().options == null || mc.getEntityRenderDispatcher().options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
            double d7 = mc.getEntityRenderDispatcher().options.fov;
            d7 = d7 / 100.0D;
            Vector3d vector3d = new Vector3d((double) i * -0.36D * d7, -0.045D * d7, 0.4D);
            vector3d = vector3d.xRot(-MathHelper.lerp(partialTicks, player.xRotO, player.xRot) * ((float) Math.PI / 180F));
            vector3d = vector3d.yRot(-MathHelper.lerp(partialTicks, player.yRotO, player.yRot) * ((float) Math.PI / 180F));
            vector3d = vector3d.yRot(f1 * 0.5F);
            vector3d = vector3d.xRot(-f1 * 0.7F);
            d4 = MathHelper.lerp(partialTicks, player.xo, player.getX()) + vector3d.x;
            d5 = MathHelper.lerp(partialTicks, player.yo, player.getY()) + vector3d.y;
            d6 = MathHelper.lerp(partialTicks, player.zo, player.getZ()) + vector3d.z;
            f3 = player.getEyeHeight();
        } else {
            d4 = MathHelper.lerp(partialTicks, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
            d5 = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) partialTicks - 0.45D;
            d6 = MathHelper.lerp(partialTicks, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
            f3 = player.isCrouching() ? -0.1875F : 0.0F;
        }
/*
        double d9 = start.x;
        double d10 = start.y;
        double d8 = start.z;
        float f4 = (float)(d4 - d9);
        float f5 = (float)(d5 - d10) + f3;
        float f6 = (float)(d6 - d8);
        Matrix4f matrix4f1 = matrixStack.last().pose();
        int j = 16;

        for(int k = 0; k < 16; ++k) {
            stringVertex(f4, f5, f6, ivertexbuilder1, matrix4f1, fraction(k, 16));
            stringVertex(f4, f5, f6, ivertexbuilder1, matrix4f1, fraction(k + 1, 16));
        }*/

        renderConnection(start, new Vector3d(d4, d5 + f3, d6), world, matrixStack.last().pose(), buffer, hightlight, reversed);

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderHandEvent(RenderHandEvent event) {
        if (!ClientConfig.customGunHands.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;

        ItemStack main = player.getMainHandItem();
        ItemStack sec = player.getOffhandItem();

        Item mainItem = main.getItem();
        Item secItem = sec.getItem();


        if (player == null) {
            return;
        }


        if (mainItem instanceof ItemGun || secItem instanceof ItemGun) {
            /*if (mc.options.getCameraType() == PointOfView.FIRST_PERSON && !mc.options.hideGui
                    && !player.isSpectator()) {*/
            event.setCanceled(true);
            ItemGun mainGun;

            if (mainItem instanceof ItemGun) {
                mainGun = (ItemGun) mainItem;
                if ((!sec.isEmpty() && !(secItem instanceof ItemGun))) {
                    return;
                }
            }
            else {
                mainGun = (ItemGun) secItem;
                if ((!main.isEmpty() && !(mainItem instanceof ItemGun))) {
                    return;
                }
            }

            if (event.getHand() == Hand.OFF_HAND) {
                return;
            }

            if (mainGun.getScoped() && mainGun.getFOVFactor(main) != 1 && mc.options.keyAttack.isDown()) {
                scoping = true;
            } else {
                scoping = false;
            }

            if (scoping) {
                return;
            }

            Animation idleAnimation = (mc.options.keyAttack.isDown() && mainGun.getFOVFactor(main) != 1) ? mainGun.getAimAnimation() : mainGun.getIdleAnimation();
            Animation shootAnimation = (mc.options.keyAttack.isDown() && mainGun.getFOVFactor(main) != 1) ? mainGun.getAimShootAnimation() : mainGun.getShootAnimation();

            if (shotAnimationTimer > System.currentTimeMillis()) {
                if (!Animations.override && Animations.currentAnimation != shootAnimation) {
                    Animations.currentAnimation = shootAnimation;
                    if (Animations.currentAnimation != null) {
                        Animations.currentAnimation.unpause();
                        Animations.currentAnimation.setRepeat(false);
                    }
                }
            } else if (idleAnimation != null && shootAnimation != null && !Animations.override && Animations.currentAnimation != idleAnimation && (Animations.currentAnimation != shootAnimation || !Animations.currentAnimation.isRunning())) {
                Animations.currentAnimation = idleAnimation;
                if (Animations.currentAnimation != null) {
                    Animations.currentAnimation.unpause();
                    Animations.currentAnimation.setRepeat(true);
                }
            }

            if (Animations.offset != null) {
                event.getMatrixStack().translate(Animations.offset.x, Animations.offset.y, Animations.offset.z);
            }

           /* ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel upperModel = itemRenderer.getModel(new ItemStack(Items.DIAMOND), mc.level, null);
            itemRenderer.render(new ItemStack(Items.DIAMOND), ItemCameraTransforms.TransformType.FIXED, true, event.getMatrixStack(), mc.renderBuffers().bufferSource(), mc.getEntityRenderDispatcher().getPackedLightCoords(player, event.getPartialTicks()), mc.getEntityRenderDispatcher().getPackedLightCoords(player, event.getPartialTicks()), upperModel);


            RenderEventHandler.renderArmWithItem(mc, player, 0, 0, Hand.MAIN_HAND, 0, player.getMainHandItem(), 0, event.getMatrixStack(), mc.renderBuffers().bufferSource(), mc.getEntityRenderDispatcher().getPackedLightCoords(player, event.getPartialTicks()));
            */
            this.renderHandsWithItems(mc, event.getPartialTicks(), event.getMatrixStack(), mc.renderBuffers().bufferSource(), player, mc.getEntityRenderDispatcher().getPackedLightCoords(player, event.getPartialTicks()), event.getHand());

            //}
        }
    }

    /*
    HAND END
     */

    public void renderHandsWithItems(Minecraft minecraft, float p_228396_1_, MatrixStack matrixStack, IRenderTypeBuffer.Impl impl, ClientPlayerEntity player, int light, Hand hand) {
        float f = player.getAttackAnim(p_228396_1_);
        //Hand hand = MoreObjects.firstNonNull(player.swingingArm, Hand.MAIN_HAND);
        float f1 = MathHelper.lerp(p_228396_1_, player.xRotO, player.xRot);
        float f3 = MathHelper.lerp(p_228396_1_, player.xBobO, player.xBob);
        float f4 = MathHelper.lerp(p_228396_1_, player.yBobO, player.yBob);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((player.getViewXRot(p_228396_1_) - f3) * 0.1F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((player.getViewYRot(p_228396_1_) - f4) * 0.1F));
        if (hand == Hand.MAIN_HAND) {
            //Right hand and item
            //float oMainHandHeight = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187470_g");
            //float mainHandHeight = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187469_f");
            ItemStack mainHandItem = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187467_d");

            //Right hand swing front-back
            //float f5 = hand == Hand.MAIN_HAND ? f : 0.0F;
            //Right hand swing up-down
            //float f2 = 1.0F - MathHelper.lerp(p_228396_1_, oMainHandHeight, mainHandHeight);

            //this.renderArmWithItem(minecraft, player, p_228396_1_, f1, Hand.MAIN_HAND, 0, mainHandItem, 0, matrixStack, impl, light);
            this.renderPlayerArm(minecraft, matrixStack, impl, light, 0f, 0f, HandSide.RIGHT, player, mainHandItem, p_228396_1_, f1);
        } else {
            //Left hand and item
            //float oOffHandHeight = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187472_i");
            //float offHandHeight = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187471_h");
            //ItemStack offHandItem = ObfuscationReflectionHelper.getPrivateValue(FirstPersonRenderer.class, minecraft.getItemInHandRenderer(), "field_187468_e");

            //Left hand swing front-back
            //float f6 = hand == Hand.OFF_HAND ? f : 0.0F;
            //Left hand swing up-down
            //float f7 = 1.0F - MathHelper.lerp(p_228396_1_, oOffHandHeight, offHandHeight);

            //this.renderArmWithItem(minecraft,player, p_228396_1_, f1, Hand.OFF_HAND, 0, offHandItem, 0, matrixStack, impl, light);
            //this.renderPlayerArm(minecraft, matrixStack, impl, light, 0, 0, HandSide.LEFT, player, offHandItem, p_228396_1_, f1);
        }
        impl.endBatch();
    }

    private void renderPlayerArm(Minecraft minecraft, MatrixStack matrix, IRenderTypeBuffer buffer, int light, float verticalRot, float horizontalRot, HandSide p_228401_6_, AbstractClientPlayerEntity player, ItemStack stack, float ff1, float ff2) {
        matrix.pushPose();


        boolean flag = p_228401_6_ != HandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(horizontalRot);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(horizontalRot * (float) Math.PI);
        matrix.translate(f * (f2 + 0.64000005F), f3 + -0.6F + verticalRot * -0.6F, f4 + -0.71999997F);
        matrix.translate(f * -0.25, -0.1, 0.0D);
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(-90.0F));
        float f5 = MathHelper.sin(horizontalRot * horizontalRot * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        matrix.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayerEntity abstractclientplayer = minecraft.player;
        minecraft.getTextureManager().bind(abstractclientplayer.getSkinTextureLocation());

        /*matrix.translate((double)(f * 5.6F), 0.0D, 0.0D);*/

        //My edit

/*
        PlayerRenderer playerrenderer = (PlayerRenderer) minecraft.getEntityRenderDispatcher().getRenderer(abstractclientplayer);
        if (flag) {


            matrix.translate(0,Math.sin(Math.toRadians(15*f))*2.75, 0);
            matrix.translate(f * -0.5F, 0.0D, 0.0D);
            //this.renderArmWithItem(minecraft, player, ff1, ff2, Hand.MAIN_HAND, 0, stack, 0, matrix, buffer, light);
        } else {
            //playerrenderer.renderLeftHand(matrix, buffer, light, abstractclientplayer);
        }*/
        if (Animations.currentAnimation != null) {
            Animations.currentAnimation.render(matrix, buffer, light);
        }
        matrix.popPose();
    }

    private void applyItemArmTransform(MatrixStack p_228406_1_, HandSide p_228406_2_, float p_228406_3_) {
        int i = p_228406_2_ == HandSide.RIGHT ? 1 : -1;
        p_228406_1_.translate((float) i * 0.56F, -0.52F + p_228406_3_ * -0.6F, -0.72F);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderPowerLines(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;
        World world = mc.level;
        PlayerEntity player = mc.player;
        if (player == null)
            return;
        IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
        MatrixStack matrixStack = event.getMatrixStack();

        boolean hightlight = PlayerUtils.canSeeEnergyFlow(player);

        Vector3d camera = mc.gameRenderer.getMainCamera().getPosition();
        matrixStack.pushPose();
        matrixStack.translate(-camera.x, -camera.y, -camera.z);
        Matrix4f matrix = matrixStack.last().pose();
        for (TileEntity te : world.blockEntityList) {
            if (!(te instanceof IVoltage)) {
                continue;
            }
            IVoltage voltage = (IVoltage) te;

            List<ElectricConnection> outputs = voltage.getOutputs();
            if (outputs != null) {
                for (ElectricConnection connection : outputs) {
                    renderConnection(connection, world, matrix, buffer, hightlight, true);
                }
            }

            List<ElectricConnection> inputs = voltage.getInputs();
            if (inputs != null) {
                for (ElectricConnection connection : inputs) {
                    if (!mc.level.isLoaded(connection.getFrom())) {
                        renderConnection(connection, world, matrix, buffer, hightlight, true);
                    }
                }
            }
        }
        matrixStack.popPose();


        /*Minecraft mc = Minecraft.getInstance();
        Vector3d camera = mc.gameRenderer.getMainCamera().getPosition();
        IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
        IVertexBuilder builder = buffer.getBuffer(RenderType.lines());
        MatrixStack stack = event.getMatrixStack();
        stack.pushPose();
        Matrix4f matrix = stack.last().pose();
        stack.translate(-camera.x, -camera.y, -camera.z);

        int blockX = 120, blockY = 70, blockZ = 1023; //Temp coordinates to test rendering

        builder.vertex(matrix, blockX, blockY, blockZ).color(1f, 1f, 1f, 1f).endVertex();
        builder.vertex(matrix, blockX + 1, blockY, blockZ).color(1f, 1f, 1f, 1f).endVertex();
        buffer.endBatch(RenderType.lines());
        builder = buffer.getBuffer(RenderType.lines());
        builder.vertex(matrix, blockX + 1, blockY, blockZ).color(1f, 1f, 1f, 1f).endVertex();
        builder.vertex(matrix, blockX + 2, blockY+1, blockZ).color(1f, 1f, 1f, 1f).endVertex();
        buffer.endBatch(RenderType.lines());
        stack.popPose(); */

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderPowerLineToEntityItems(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;
        World world = mc.level;
        PlayerEntity player = mc.player;
        if (player == null)
            return;
        IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
        MatrixStack matrixStack = event.getMatrixStack();

        boolean hightlight = PlayerUtils.canSeeEnergyFlow(player);

        Vector3d camera = mc.gameRenderer.getMainCamera().getPosition();
        matrixStack.pushPose();
        matrixStack.translate(-camera.x, -camera.y, -camera.z);
        Matrix4f matrix = matrixStack.last().pose();

        for (ItemEntity entity : world.getLoadedEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(player.blockPosition()).inflate(16))) {
            ItemStack stack = entity.getItem();
            if (stack.isEmpty())
                continue;
            if (stack.getItem() instanceof ItemWire) {
                CompoundNBT nbt = stack.getOrCreateTag();
                if (nbt == null)
                    continue;
                if (nbt.contains("from", Constants.NBT.TAG_LONG)) {
                    BlockPos from = BlockPos.of(nbt.getLong("from"));

                    double entityX = entity.xOld
                            + (entity.getX() - entity.xOld) * (double) event.getPartialTicks();
                    double entityY = entity.yOld
                            + (entity.getY() - entity.yOld) * (double) event.getPartialTicks();
                    double entityZ = entity.zOld
                            + (entity.getZ() - entity.zOld) * (double) event.getPartialTicks();


                    renderConnection(new Vector3d(from.getX() + 0.5d, from.getY() + 0.5d, from.getZ() + 0.5d),
                            new Vector3d(entityX, entityY + entity.getBbHeight(), entityZ), world, matrix, buffer, hightlight, true);


                } else if (nbt.contains("to", Constants.NBT.TAG_LONG)) {
                    BlockPos to = BlockPos.of(nbt.getLong("to"));
                    double entityX = entity.xOld
                            + (entity.getX() - entity.xOld) * (double) event.getPartialTicks();
                    double entityY = entity.yOld
                            + (entity.getY() - entity.yOld) * (double) event.getPartialTicks();
                    double entityZ = entity.zOld
                            + (entity.getZ() - entity.zOld) * (double) event.getPartialTicks();

                    renderConnection(new Vector3d(to.getX() + 0.5d, to.getY() + 0.5d, to.getZ() + 0.5d),
                            new Vector3d(entityX, entityY + entity.getBbHeight(), entityZ), world, matrix, buffer, hightlight, false);


                }
            }
        }

        matrixStack.popPose();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderPowerLineToHand(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;
        World world = mc.level;
        PlayerEntity player = mc.player;
        if (player == null)
            return;

        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();

        if (mainStack.isEmpty() && offStack.isEmpty())
            return;

        Item mainItem = mainStack.getItem();
        Item offItem = offStack.getItem();

        IRenderTypeBuffer.Impl buffer = mc.renderBuffers().bufferSource();
        MatrixStack matrixStack = event.getMatrixStack();

        boolean hightlight = PlayerUtils.canSeeEnergyFlow(player);

        Vector3d camera = mc.gameRenderer.getMainCamera().getPosition();
        matrixStack.pushPose();
        matrixStack.translate(-camera.x, -camera.y, -camera.z);

        if (mainItem instanceof ItemWire) {
            CompoundNBT nbt = mainStack.getOrCreateTag();
            if (nbt.contains("to", Constants.NBT.TAG_LONG)) {
                BlockPos to = BlockPos.of(nbt.getLong("to"));
                renderConnectionToHand(new Vector3d(to.getX() + 0.5d, to.getY() + 0.5d, to.getZ() + 0.5d),
                        player, world, matrixStack, buffer, event.getPartialTicks(), true, false);

            } else if (nbt.contains("from", Constants.NBT.TAG_LONG)) {
                BlockPos from = BlockPos.of(nbt.getLong("from"));
                renderConnectionToHand(new Vector3d(from.getX() + 0.5d, from.getY() + 0.5d, from.getZ() + 0.5d),
                        player, world, matrixStack, buffer, event.getPartialTicks(), true, true);
            }

        }

        matrixStack.popPose();
    }


    private enum EnumHandPos {

        NONE(Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO),
        PISTOL_ONE(Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO),
        PISTOL_DUAL(Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO),
        LONG_ONE(Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO, Vector3d.ZERO);

        Vector3d rightHandRotation;
        Vector3d leftHandRotation;
        Vector3d rightHandPosition;
        Vector3d leftHandPosition;

        EnumHandPos(Vector3d rightHandRotation,
                    Vector3d leftHandRotation,
                    Vector3d rightHandPosition,
                    Vector3d leftHandPosition) {
            this.rightHandRotation = rightHandRotation;
            this.leftHandRotation = leftHandRotation;
            this.rightHandPosition = rightHandPosition;
            this.leftHandPosition = leftHandPosition;
        }
    }

}
