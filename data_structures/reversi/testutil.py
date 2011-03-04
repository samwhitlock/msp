import os, re, sys
import signal
from threading import Timer
from subprocess import Popen, PIPE
from tempfile import mkstemp

def safeKill(proc):
    try:
        os.kill(proc.pid, signal.SIGHUP)
    except:
        pass

def killProcs(flag, *procs):
    flag[0] = True
    for proc in procs:
        safeKill(proc)

def sleep(secs):
    timer = Timer (secs, lambda: None)
    timer.start()
    timer.join()

def safeOpen(name):
    try:
        return open(name)
    except IOError:
        return None

def safeClose(f):
    try:
        f.close()
    except:
        pass

def safeRemove(f):
    try:
        os.remove(f)
    except:
        pass

def contents(name):
    try:
        inp = open(name)
        val = inp.read()
        inp.close()
        return val
    except:
        return None

def extractSpec(out):
    if out is None:
        return None
    val = '\n'.join(re.findall(r'(?ms) *(?:===.*?^ *=== *$|\bBlack wins\. *$'
                               r'|\bWhite wins\. *$|\bDraw\. *$)', out))
    return re.sub(r'(?m)^ *| *$', '', val)
                     
def extractStdSpec(out):
    if out is None:
        return None
    val = '\n'.join(re.findall(r'(?ms) *(?:===.*?^ *=== *$|\bBlack wins\. *$'
                               r'|\bWhite wins\. *$|\bDraw\. *$|^<OUTCOME> *$)', out))
    return re.sub(r'(?m)^ *| *$', '', val)
                     
def modWinner(out):
    return re.sub(r'Black wins\.|White wins\.|Draw\.', '<OUTCOME>', out)

def getLimit(limits, name, dflt):
    mat = re.search (r'\b%s\s*=\s*(\d+)', limits or '')
    if mat:
        return int(mat.group(1))
    else:
        return dflt
    
class LimitedPopen(Popen):
    def __init__(self, args, cpulimit=30, filesizelimit=500, stdinput=None,
                 **keys):
        self.results = None
        self.tempout = self.temperr = self.tempin = None

        args = "limited-exec %s %s %s" % (cpulimit, filesizelimit, args)

        if keys.get('stdout', None) or PIPE == PIPE:
            self.tempout = mkstemp()
            keys['stdout'] = self.tempout[0]
        if keys.get('stderr', None) or PIPE == PIPE:
            self.temperr = mkstemp()
            keys['stderr'] = self.temperr[0]
        if stdinput is not None:
            self.tempin = mkstemp()
            os.write(self.tempin[0], stdinput)
            os.lseek(self.tempin[0], 0, 0)
            keys['stdin'] = self.tempin[0]

        Popen.__init__ (self, re.split(r'\s+', args),  **keys)

    def communicate(self, input=None):
        if not self.results:
            out, err = Popen.communicate(self, input)
            if self.tempout:
                safeClose(self.tempout[0])
                out = contents(self.tempout[1])
                safeRemove(self.tempout[1])
            if self.temperr:
                safeClose(self.temperr[0])
                err = contents(self.temperr[1])
                safeRemove(self.temperr[1])
            if self.tempin:
                safeClose(self.tempin[0])
                safeRemove(self.tempin[1])
            self.results = out, err
        return self.results
    
