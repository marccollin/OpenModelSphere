"""\
Equivalent to the ../awt/simple.py example but using swing components
instead of AWT ones.
"""

import java
# This line will import the appropriate swing library for your system (jdk 1.1 or 1.2)
from pawt import swing


def exit(e): java.lang.System.exit(0)

frame = swing.JFrame('Swing Example', visible=1)
button = swing.JButton('Close Me!', actionPerformed=exit)
frame.contentPane.add(button)
frame.pack()
