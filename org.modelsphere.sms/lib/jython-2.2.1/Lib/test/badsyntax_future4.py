"""This is a test"""
from __future__ import nested_scopes

import __future__


def f(x):
    def g(y):
        return x + y
    return g

print f(2)(4)
