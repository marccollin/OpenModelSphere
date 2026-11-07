import sys
print 'Testing Jython Version', sys.version,'on',sys.platform

from test_support import *

print_test('Core Python Language', 0)

#import test_grammar
#import test_builtin
#import test_exceptions

print_test('Standard Extension Modules', 0)

print_test('Integration with Java', 0)

import test_jarray

print 'All old-style tests completed successfully!'

print "Executing unittest tests"

import unittest
unittest.main() # calls SystemExit
