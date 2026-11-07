#! /usr/bin/env python
"""Simple test script for cryptmodule.c
   Roger E. Masse
"""

import crypt

from test_support import verbose

c = crypt.crypt('mypassword', 'ab')
if verbose:
    print 'Test encryption: ', c
