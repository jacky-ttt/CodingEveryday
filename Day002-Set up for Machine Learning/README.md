# Day002
day2 of #CodingEveryday

(posted on https://medium.com/@ma010hkchun)
Day002
In Day001, I have to uninstall and reinstall the anaconda in order to use pydot in visualizing the decision tree. The process is pain in the ass. I hate it so much. I hate every configuration error I have to fix before I move on. So much unnecessary time is wasted in such painful process.
After the uninstall and reinstall of anaconda, I found the python command (e.g. python hello-world.py)can work no more. I know it is an environment variable problem, but I just don’t know where to look in my mac. I spent a few hours just for fixing this issue, until I find this
How to uninstall conda on mac? · Issue #672 · conda/conda-build

conda-build - Commands and tools for building conda packages
github.com	
Turns out, you have to change the PATH variable in .bash_profile located in user’s root directory, i.e. /User/{username}/ in mac
By changing the PATH from:
export PATH="//anaconda/bin:$PATH"
to
export PATH="/Users/{username}/anaconda/bin:$PATH"
it directs to the location anaconda2(python 2.7) is installed.
After changing the variable, remember to relaunch the terminal you are using for the change to be applied.
use below code to check python’s version (http://conda.pydata.org/docs/py2or3.html#use-a-different-version-of-python)
python —-version
=> Python 2.7.11 :: Anaconda 4.0.0 (x86_64)
— — — — — — — — — — — — — — — — — — — — — — — — — — —
BTW, to uninstall anaconda in macOS, you just have to remove the whole directory of anaconda, described in https://docs.continuum.io/anaconda/install#os-x-uninstall
— — — — — — — — — — — — — — — — — — — — — — — — — — —
encounted below error
ImportError: No module named pydot
sol:
conda install -c anaconda pydot=1.0.28
https://anaconda.org/anaconda/pydot