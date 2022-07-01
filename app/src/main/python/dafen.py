from java import jclass
from scipy.stats import norm
from java.util import ArrayList

def beatscore (beats) :
    #均值Ameans,1是心率
    Ameans1 = norm.pdf(1, loc=1, scale=0.5)
    # 分数goals,y是输入数据，x是对y进行处理,理想心率是55-70，取中间值
    y1 = beats
    p = []
    n = [0.2]
    goals2 = []
    for z in range(5):

        if y1[z] < 55:
            x1 = y1[z] / 120
            if y1[z] < 50:
                lowb = 1
                highb = 0
        else:
            x1 = y1[z] / 62.5
            if y1 > 90:
                lowb = 0
                highb = 1
        goals1 = norm.pdf(x1, loc=1, scale=0.5)
        p.append(100*goals1/Ameans1)
        if z < 4:
            n.append((norm.pdf(1-(y1[z+1]-y1[z]), loc=1, scale=0.5)+1)*0.2)
    for z1 in range(5):
        goals2.append(p[z1] * n[z1] / sum(n))
    ot1 = sum(goals2)
    ot2 = int(ot1)
    return [ot1, highb, lowb]

def oxygenscore (oxygens) :
    y2 = oxygens
    p = []
    n = [0.2]
    goals2 = []
    for z in range(5):
            if y2 >= 85:
                x2 = y2-85
            else:
                x2 = 0
            p.append(int(100*x2/15))
            if z < 4:
                n.append((norm.pdf(1-(y2[z+1]-y2[z]), loc=1, scale=0.5)+1)*0.2)
            if y2 < 95:
                lowo = 1
    for z1 in range(5):
            goals2.append(p[z1] * n[z1] / sum(n))
    ot2 = int(sum(goals2))
    return [ot2, lowo]

def pressscore (lowpress,highpress) :
    Ameans31 = norm.pdf(1, loc=1, scale=0.3)
    p = []
    n = [0.2]
    goals2 = []
    y31 = highpress
    Ameans32 = norm.pdf(1, loc=1, scale=0.25)
    y32 = lowpress
    for z in range(5):
        if y31[z] > 90:
            if y31[z] > 140:
                highp = 1
                lowp = 0
            x31 = (300-1.5*y31[z])/120
        else:
            x31 = y31[z] / 138
            if y31[z] < 90:
                highp = 0
                lowp = 1
        goals1 = norm.pdf(x31, loc=1, scale=0.3)
        if goals31 < 80:
            goals31 = 0
        ot31 = int(100*goals31/Ameans31)
        if y32[z] > 80:
            if y32[z] <= 90:
                x32 = (240-2*y32[z])/80
            else:
                lowp = 0
                highp = 1
                x32 = (1+0.0025*(y32[z]-90))*(1+0.0025*(y32[z]-90))*(1-0.0027*y32[z])*(162-0.8*y32[z])/90
        else:
            x32 = y32[z] / 80
            if y32[z] <=60:
                lowp = 1
                highp = 0
        goals32 = norm.pdf(x32, loc=1, scale=0.25)
        ot32 = int(100*goals32/Ameans32)
        if ot31 > ot32:
            ot3 = ot32
        else:
            ot3 = ot31
        p.append(ot3)
        if z < 4:
            n.append((norm.pdf(1-(y32[z+1]-y32[z]), loc=1, scale=0.5)+1)*0.2)
    for z1 in range(5):
            goals2.append(p[z1] * n[z1] / sum(n))
    ot2 = int(sum(goals2))
    return [ot2, highp, lowp]

def sugarscore (sugars):
    p = []
    n = [0.2]
    goals2 = []
    Ameans = norm.pdf(1, loc=1, scale=0.2)
    y =sugars
    for z in range(5):
        if y[z] <= 9.5:
            x = (14.25-0.5*y[z])/9.5
            if y[z] <8:
                lows = 1
                highs = 0
        else:
            x = (1+0.03*(y[z]-9.5)*(y[z]-9.5))*(1-0.022*(y[z]-9.5)*(y[z]-9.5))*(3+0.7*y[z])/9.5
            if y[z] > 11:
                lows = 0
                highs = 1
        goals = norm.pdf(x, loc=1, scale=0.2)
        p.append(goals)
        if z < 4:
            n.append((norm.pdf(1-(y[z+1]-y[z]), loc=1, scale=0.5)+1)*0.2)
    for z1 in range(5):
                goals2.append(p[z1] * n[z1] / sum(n))
    ot2 = int(sum(goals2))
    return [ot2, highs, lows]

