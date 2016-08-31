# Open Pixel Control Library 

[![Build Status](https://travis-ci.org/akinaru/opc-java.svg?branch=master)](https://travis-ci.org/akinaru/opc-java)
[ ![Download](https://api.bintray.com/packages/akinaru/maven/opc-java/images/download.svg) ](https://bintray.com/akinaru/maven/opc-java/_latestVersion)
[![License](http://img.shields.io/:license-mit-blue.svg)](LICENSE.md)

Open Pixel Control Library for Java/Android forked from https://github.com/scanlime/fadecandy/tree/master/examples/java

<hr/>

Simple Java OPC client for the [Fadecandy](http://www.misc.name/fadecandy/).
This is an alternative to the Processing client, providing a simpler interface similar to Adafruit's [NeoPixel library for Arduino](https://github.com/adafruit/Adafruit_NeoPixel).

## Writing simple pixel programs

Here is a simple Java program that will color some of the lights:

```
    public static void main(String[] args) throws Exception {
        OpcClient server = new OpcClient("10.28.46.61", 7890);
        OpcDevice fadecandy = server.addDevice();
        PixelStrip strip = fadecandy.addPixelStrip(0, 64);
        
        int color = 0xFF0000;  // red
        strip.setPixelColor(3, color);
        strip.setPixelColor(5, 0x888800); // yellow
        strip.setPixelColor(7, 0x00FF00); // green
        
        server.show();        // Display the pixel changes
        Thread.sleep(5000);   // Wait five seconds
        server.clear();       // Set all pixels to black
        server.show();        // Show the darkened pixels
        
        server.close();
    }
```

## Animations
We can also create Animation classes which will draw different patterns over and over.  We can attach animations to any given pixel strip.  When we attach a new animation object, the light patterns will change.

```
public class RandomPixels extends Animation {

    java.util.Random rand;
    
    public void reset(PixelStrip strip) {
        rand = new java.util.Random();
    }

    public boolean draw(PixelStrip strip) {
        int randomPixel = rand.nextInt(strip.getPixelCount());
        int randomColor = makeColor(rand.nextInt(255), 
                         rand.nextInt(255), rand.nextInt(255));
        strip.setPixelColor(randomPixel, randomColor);
        return true;
    }
}
```

## Server Configuration

The Fadecandy has eight pins, each of which can control a strip of 64 pixels.
If you have contiguous strips of pixels starting on pin zero, then your `fc_server` won't need to map the output pixels numbers to configurable OPC numbers.  However, if you have
shorter strips connected to different pins, you may want to set up a [JSON configuration file](https://github.com/scanlime/fadecandy/blob/master/doc/fc_server_config.md)
for the server.  The `OpcClient` class can print out a JSON configuration based on
strip definitions:

```
public static void main(String[] arg)  {
    OpcClient server = new OpcClient("raspberrypi.local", 7980);
    OpcDevice fadeCandy = server.addDevice();
        
    PixelStrip strip1 = fadeCandy.addPixelStrip(0, 64);  // 8 x 8 grid on pin 0
    PixelStrip strip2 = fadeCandy.addPixelStrip(1, 8);   // 8 pixel strip on pin 1
    PixelStrip strip3 = fadeCandy.addPixelStrip(2, 16);  // 16 pixel ring on pin 2
    PixelStrip strip4 = fadeCandy.addPixelStrip(3, 24);  // The first 24 pixels on pin 3
    PixelStrip strip5 = fadeCandy.addPixelStrip(3, 17);  // The next 17 pixels on pin 3
        
    // Since the pixels are not uniform strips of 64, customize 
    // the server config JSON file with the following:
    System.out.println(server.getConfig());
}
```

For the above setup, the JSON server configuration should be:

```
{
    "listen": [ "raspberrypi.local", 7890],
    "verbose": true,
    "devices": [
        {
            "type": "fadecandy",
            "map": [

                    [0, 0, 0, 64 ],
                    [0, 64, 64, 8 ],
                    [0, 72, 128, 16 ],
                    [0, 88, 192, 24 ],
                    [0, 112, 216, 17 ]
                ]
        }   ]
}
```

## Examples


* Example animation that pulses all pixels through an array of colors

```
./gradlew pulsing
```

* Display a moving white pixel with trailing orange/red flames. This looks pretty good with a ring of pixels.

```
./gradlew spark
```

* Pairs of lights traveling down the strip.

```
./gradlew theaterLights
```

## Build

Gradle using IntelliJ IDEA or Eclipse

## License

```
The MIT License (MIT)

Copyright for portions of project opc-java are held by Micah Elizabeth Scott, 2013 
as part of project Fadecandy. 
All other copyright for project opc-java are held by Bertrand Martel, 2016.

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```