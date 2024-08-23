# VickyE's Project Korra Addon
**Currently in Development**


~~MUHUHAHAHAHAHA THIS IS MY README......ill put something useful here later~~

### Version 0.0.1:
**Particle System**
- (ParticleTask.java) was made for easy addition of particles to moves instead of using the default project korra 'Element Particle System' or there about.
- System allows for use of both redstone and dust particle transition. but it is to note that when entering parameters the first color is for the REDSTONE and the other two colors are for the DUST_COLOR_TRANSITION
- The System also implements a ParticleEffect(Pattern) Module where from a list of in-built Presets u can create Paths for the particles to follow
- Because of the sheer complexity of this System the parameters needed are a GODLY amount.
```java
    public ParticleTask(long startTime, Arrow arrow, double radiusH, double radiusM, double heightStep, Color headColor, Color transitionColorStart, Color transitionColorEnd,
                        int headCount, int middleCount,
                        double spreadXH, double spreadYH, double spreadZH,
                        double spreadXM, double spreadYM, double spreadZM,
                        float speedH, float speedM,
                        long lagBehind, double backwardVelocity,
                        float sizeH, float sizeM,
                        Particle particleH, Particle particleM,
                        ParticleTypeEffect.ParticleTypeEffects effectTypeH, ParticleTypeEffect.ParticleTypeEffects effectTypeM,
                        double rFreq, double pFreq, double angleStep, ParticleTypeEffect.SpacingMode spacingMode, int circleNumber,
                        float yaw, float pitch)
```
