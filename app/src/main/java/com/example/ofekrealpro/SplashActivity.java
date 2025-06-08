package com.example.ofekrealpro;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private AnimatedSplashView splashView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        splashView = new AnimatedSplashView(this);
        setContentView(splashView);

        handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, loginS.class);
            startActivity(intent);
            finish();
        }, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashView != null) {
            splashView.stopAnimation();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private class AnimatedSplashView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder surfaceHolder;
        private AnimationThread animationThread;
        private boolean isAnimating = false;

        // Animation properties
        private float logoScale = 0f;
        private float logoRotation = 0f;
        private float waveOffset = 0f;
        private float particleTime = 0f;
        private float textAlpha = 0f;
        private float backgroundHue = 0f;

        // Particles
        private List<Particle> particles;
        private Random random;

        // Paint objects
        private Paint logoPaint;
        private Paint wavePaint;
        private Paint particlePaint;
        private Paint textPaint;
        private Paint backgroundPaint;

        public AnimatedSplashView(SplashActivity context) {
            super(context);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);

            random = new Random();
            particles = new ArrayList<>();

            initializePaints();
            initializeParticles();
            setupAnimations();
        }

        private void initializePaints() {
            // Logo paint
            logoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            logoPaint.setColor(Color.WHITE);
            logoPaint.setStyle(Paint.Style.FILL);

            // Wave paint
            wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            wavePaint.setStyle(Paint.Style.FILL);

            // Particle paint
            particlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            particlePaint.setStyle(Paint.Style.FILL);

            // Text paint
            textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(48f);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setFakeBoldText(true);

            // Background paint
            backgroundPaint = new Paint();
        }

        private void initializeParticles() {
            // We'll initialize particles in surfaceChanged when we have dimensions
        }

        private void setupAnimations() {
            // Logo scale animation
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0f, 1f);
            scaleAnimator.setDuration(1500);
            scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            scaleAnimator.addUpdateListener(animation -> logoScale = (float) animation.getAnimatedValue());
            scaleAnimator.start();

            // Text fade in animation
            ValueAnimator textAnimator = ValueAnimator.ofFloat(0f, 1f);
            textAnimator.setStartDelay(1000);
            textAnimator.setDuration(1500);
            textAnimator.addUpdateListener(animation -> textAlpha = (float) animation.getAnimatedValue());
            textAnimator.start();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isAnimating = true;
            animationThread = new AnimationThread();
            animationThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Initialize particles now that we have screen dimensions
            particles.clear();
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(width, height));
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopAnimation();
        }

        public void stopAnimation() {
            isAnimating = false;
            if (animationThread != null) {
                boolean retry = true;
                while (retry) {
                    try {
                        animationThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        private void drawFrame(Canvas canvas) {
            if (canvas == null) return;

            int width = canvas.getWidth();
            int height = canvas.getHeight();

            // Update animation time
            waveOffset += 0.03f;
            particleTime += 0.02f;
            backgroundHue += 0.3f;
            logoRotation += 0.5f;
            if (backgroundHue > 360) backgroundHue = 0;
            if (logoRotation > 360) logoRotation = 0;

            // Draw animated background
            drawAnimatedBackground(canvas, width, height);

            // Draw animated waves
            drawWaves(canvas, width, height);

            // Draw particles
            drawParticles(canvas, width, height);

            // Draw logo
            drawLogo(canvas, width, height);

            // Draw text
            drawText(canvas, width, height);
        }

        private void drawAnimatedBackground(Canvas canvas, int width, int height) {
            // Create animated gradient background
            float[] hsv1 = {backgroundHue, 0.7f, 0.4f};
            float[] hsv2 = {(backgroundHue + 80) % 360, 0.8f, 0.6f};
            float[] hsv3 = {(backgroundHue + 160) % 360, 0.6f, 0.3f};

            int color1 = Color.HSVToColor(hsv1);
            int color2 = Color.HSVToColor(hsv2);
            int color3 = Color.HSVToColor(hsv3);

            LinearGradient gradient = new LinearGradient(
                    0, 0, width, height,
                    new int[]{color1, color2, color3},
                    new float[]{0f, 0.5f, 1f},
                    Shader.TileMode.CLAMP
            );

            backgroundPaint.setShader(gradient);
            canvas.drawRect(0, 0, width, height, backgroundPaint);
        }

        private void drawWaves(Canvas canvas, int width, int height) {
            // Draw multiple wave layers
            for (int layer = 0; layer < 3; layer++) {
                Path wavePath = new Path();
                float amplitude = 40 + layer * 25;
                float frequency = 0.008f + layer * 0.003f;
                float phaseShift = waveOffset + layer * 1.5f;
                float baseHeight = height - 150 - layer * 80;

                wavePath.moveTo(0, height);

                for (int x = 0; x <= width; x += 8) {
                    float y = baseHeight + (float) (amplitude * Math.sin(x * frequency + phaseShift));
                    if (x == 0) {
                        wavePath.moveTo(x, y);
                    } else {
                        wavePath.lineTo(x, y);
                    }
                }

                wavePath.lineTo(width, height);
                wavePath.lineTo(0, height);
                wavePath.close();

                int alpha = (int)(80 - layer * 20);
                int waveColor = Color.argb(alpha, 255, 255, 255);
                wavePaint.setColor(waveColor);
                canvas.drawPath(wavePath, wavePaint);
            }
        }

        private void drawParticles(Canvas canvas, int width, int height) {
            for (Particle particle : particles) {
                particle.update(particleTime, width, height);

                particlePaint.setColor(particle.color);
                particlePaint.setAlpha((int)(particle.alpha * 255));

                canvas.drawCircle(particle.x, particle.y, particle.size, particlePaint);
            }
        }

        private void drawLogo(Canvas canvas, int width, int height) {
            canvas.save();

            float centerX = width / 2f;
            float centerY = height / 2f - 80;

            canvas.translate(centerX, centerY);
            canvas.scale(logoScale, logoScale);
            canvas.rotate(logoRotation * 0.2f);

            // Draw geometric logo
            drawGeometricLogo(canvas);

            canvas.restore();
        }

        private void drawGeometricLogo(Canvas canvas) {
            // Outer ring
            logoPaint.setColor(Color.WHITE);
            logoPaint.setStyle(Paint.Style.STROKE);
            logoPaint.setStrokeWidth(6f);
            canvas.drawCircle(0, 0, 70, logoPaint);

            // Inner hexagon
            Path hexPath = new Path();
            for (int i = 0; i < 6; i++) {
                float angle = (float) (i * Math.PI / 3);
                float x = (float) (50 * Math.cos(angle));
                float y = (float) (50 * Math.sin(angle));
                if (i == 0) {
                    hexPath.moveTo(x, y);
                } else {
                    hexPath.lineTo(x, y);
                }
            }
            hexPath.close();

            logoPaint.setStyle(Paint.Style.FILL);
            logoPaint.setColor(Color.argb(200, 255, 255, 255));
            canvas.drawPath(hexPath, logoPaint);

            // Center circle
            logoPaint.setColor(Color.WHITE);
            canvas.drawCircle(0, 0, 20, logoPaint);

            // Inner dot
            logoPaint.setColor(Color.argb(150, 0, 0, 0));
            canvas.drawCircle(0, 0, 8, logoPaint);
        }

        private void drawText(Canvas canvas, int width, int height) {
            if (textAlpha <= 0) return;

            textPaint.setAlpha((int)(textAlpha * 255));

            String appName = "OferRealPro";
            String tagline = "Your Professional Solution";

            float centerX = width / 2f;
            float centerY = height / 2f + 100;

            // Main app name
            textPaint.setTextSize(64f);
            canvas.drawText(appName, centerX, centerY, textPaint);

            // Tagline
            textPaint.setTextSize(28f);
            textPaint.setAlpha((int)(textAlpha * 180));
            canvas.drawText(tagline, centerX, centerY + 60, textPaint);
        }

        private class AnimationThread extends Thread {
            @Override
            public void run() {
                while (isAnimating) {
                    Canvas canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas();
                        if (canvas != null) {
                            drawFrame(canvas);
                        }
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                    try {
                        Thread.sleep(16); // ~60 FPS
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        private class Particle {
            float x, y, velocityX, velocityY, size, alpha;
            int color;
            float phase, speed;

            public Particle(int width, int height) {
                reset(width, height);
            }

            public void reset(int width, int height) {
                if (width <= 0 || height <= 0) {
                    width = 1080; // fallback
                    height = 1920;
                }

                x = random.nextFloat() * width;
                y = random.nextFloat() * height;
                velocityX = (random.nextFloat() - 0.5f) * 1.5f;
                velocityY = (random.nextFloat() - 0.5f) * 1.5f;
                size = random.nextFloat() * 6 + 3;
                alpha = random.nextFloat() * 0.7f + 0.3f;
                phase = random.nextFloat() * (float) Math.PI * 2;
                speed = random.nextFloat() * 0.03f + 0.01f;

                // Bright colors
                float hue = random.nextFloat() * 360;
                float[] hsv = {hue, 0.8f, 1f};
                color = Color.HSVToColor(hsv);
            }

            public void update(float time, int width, int height) {
                // Smooth floating motion
                x += velocityX + (float) Math.sin(time * speed + phase) * 0.8f;
                y += velocityY + (float) Math.cos(time * speed + phase * 1.3f) * 0.6f;

                // Wrap around screen
                if (x < -size) x = width + size;
                if (x > width + size) x = -size;
                if (y < -size) y = height + size;
                if (y > height + size) y = -size;

                // Pulsing alpha
                alpha = 0.4f + 0.4f * (float) Math.sin(time * speed * 2 + phase);
            }
        }
    }
}