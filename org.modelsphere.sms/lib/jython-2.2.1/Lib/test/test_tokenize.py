import os
import sys
import tokenize

from test_support import verbose, findfile

if verbose:
    print 'starting...'
file = open(findfile('tokenize_tests'+os.extsep+'py'))
tokenize.tokenize(file.readline)
if verbose:
    print 'finished'
