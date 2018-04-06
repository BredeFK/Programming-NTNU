# Lab03: Sensors

## Author: Brede Fritjof Klausen

## The idea

Create an app consisting of a single activity, that only works in a single mode: landscape. The activity will have a black rectangle drawn on a white background with a small margin on the outside of the square. In other words, there will be a small gap between the inner rectangle drawn on the screen and the phone outside edge.

In the middle, in the initial position, draw a circle, or render an image, that is roughly 5% of the width of the screen. The circle (or image) will move around and bounce off the black boundery (you can use animations or move the view yourself, pixel by pixel). No external libraries allowed, use only Java and Android drawing APIs.

The movement will be controlled by the phone sensors (gyroscope, accelerometer, gravity vector). The task is to detect the tilt of the phone, and move the "ball" towards the lowest point of the phone tilt.

The app will generate additional events upon ball hitting the boundary rectangle. The app will make the phone emit a "ping" sound, and you the phone will vibrate just enough to signal the ball bouncing off the boundary. This will provide additional audio and haptic feedback to the user. Those additional events are easy to program - few additional lines of code.

### Optional, tilt magnitude

The initial app is controlled by the direction of the tilt, and it does not require the ball to accelerate based on the magnitute of the tilt - small and large tilt will move the ball the same way. Those students who want to make the experience more game-like, can introduce the "force vector" and accelerate the ball more when the tilt is larger, and less when the tilt is smaller, allowing the user to slow down or speed up the ball, based on the magnitude of the tilt. This requires a direct control of acceleration/de-acceleration of the ball, that will control the speed, not controlling the speed directly. The animation loop would need to be a bit more complex.


## Technologies

* Sensors
* Permissions
* "Free" drawing on canvas, textures, animations
* Media play (audio)
* Vibrations (haptics)

## Checklist

* [X] The repo URL is in correct format. `git clone http://prod3.imt.hig.no/BredeFK/Lab03`
* [ ] The code is well written, structured, and Linter warnings are non existed, or well documented and properly justified.
* [X] The app starts with the "ball" in the middle of the screen, and rectangle around the screen with the gap to the outside edge of the phone.
* [ ] When the phone is placed flat on the table, and the app is started, the "ball" does not move much from the center (understandably, the "move much" is a subjective test, discuss the result with the author of the app).
* [X] When the phone is held in a hand, the "ball" can be controlled by tilting the phone, and the ball moves as if pulled by the Earth gravity. Discuss the "control feel" of the ball movement with the author.
   * [X] No lag, immediate feedback.
   * [ ] Some lag, acceptable.
   * [ ] Large lag, non-acceptable.
* [X] When the ball hits the drawn edge, there is a "ping" sound. The delay between the visual event, i.e. the ball hitting the wall, and the sound, is "acceptable". If the delay is laggy, discuss with the author of the app.
   * [X] No lag, immediate feedback.
   * [ ] Some lag, acceptable.
   * [ ] Large lag, non-acceptable.
* [X] When the ball hits the edge, there is a short vibration event that provides haptic feedback to the user.
   * [X] No lag, immediate feedback.
   * [ ] Some lag, acceptable.
   * [ ] Large lag, non-acceptable.
* [X] Bonus: the magnitude of the tilt speeds up or slows down the ball movement.

## Hints

TBA

## Sources

- https://gist.github.com/Jawnnypoo/fcceea44be628c2d5ae1
- https://stackoverflow.com/questions/20264268/how-do-i-get-the-height-and-width-of-the-android-navigation-bar-programmatically
