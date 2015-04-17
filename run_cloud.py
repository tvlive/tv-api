#!/usr/local/bin/python
import subprocess

def request_to_app():
    import urllib
    resp = urllib.urlopen('http://localhost:9000')
    print("request to http://localhost:9000 is " + str(resp.getcode()))
    return resp.getcode()

app_pid = subprocess.Popen(["sbt", "-Drun.mode=Stub", "run"])
import time

code = 0
while code != 200:
    try:
        code = request_to_app()
    except:
        print("App not ready")
        import time
        time.sleep(5)


acceptance_pid = subprocess.Popen(["sbt", "acceptance:test"])
code_acceptance = acceptance_pid.wait()
app_pid.terminate()

