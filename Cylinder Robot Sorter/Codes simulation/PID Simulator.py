import turtle
import math
import time
import random

simu = turtle.getscreen()
simu.title = ("PID Simulator")
turtle.hideturtle()



class Motor() :
    def __init__(self, color, _entraxe):

        self.entraxe = _entraxe
        self.motorR = turtle.Turtle()
        self.motorR.speed(6)
        self.motorR.penup()
        self.motorR.goto(0, self.entraxe/2)
        self.motorR.pendown()
        self.motorR.color(color)

        self.motorL = turtle.Turtle()
        self.motorL.speed(6)
        self.motorL.penup()
        self.motorL.goto(0, - self.entraxe/2)
        self.motorL.pendown()
        self.motorL.color(color)

        self.theta = 0
        self.l = 0
        self.center = (0, 0)
        self.alpha = 0

        self.theta_error = 0
        self.somme_theta_error = 0
        self.delta_theta_error = 0
        self.last_theta_error = 0

        self.l_error = 0
        self.somme_l_error = 0
        self.delta_l_error = 0
        self.last_l_error = 0

        self.l_order = 0
        self.theta_order = 0

        self.uMax = 6
        self.u0 = 0
        self.diametre = 0.08

        self.vCroisiere = 0
        self.acc = 0
        self.l_evo = 0


        self.wCroisiere = 0
        self.theta_pp = 0
        self.theta_evo = 0
        self.t_total1 = 0
        self.t_total2 = 0

        self.p = 0

        self.speed_t = 0
        self.speed_theta = 0
        self.random = 0

        self.tf = 0
        self.ti = time.time()

        self.uvCroisiere = 5
        self.uwCroisiere = 1

        self.last_speed_theta = 0
        self.last_speed_l = 0
        self.delta_speed_l = 0
        self.delta_speed_theta = 0






    def odometry(self):
        self.center = ((self.motorR.pos()[0] + self.motorL.pos()[0])/2, (self.motorR.pos()[1] + self.motorL.pos()[1])/2)
        self.alpha = self.motorL.heading()
        if self.alpha > 180 :
            self.alpha = self.alpha - 360

    def consigne(self, x_cible, y_cible):
        self.odometry()
        xR, yR = self.motorR.position()
        xL, yL = self.motorL.position()
        #print("posR :", xR, yR)
        #print("posL :", xL, yL)
        x = (xR + xL)/2
        y = (yR + yL)/2
        self.center = (x, y)
        print('center:', self.center)
        self.l = ((x_cible - self.center[0])**2 + (y_cible - self.center[1])**2)**(1/2)
        #print("alpha", self.alpha)
        self.theta = (math.atan2(y_cible - self.center[1], x_cible - self.center[0]))*(180/math.pi) - self.alpha
        if self.theta > 180 :
            self.theta -= 360
        print('thetaOK:', self.theta)
        print('l:', self.l)


    def staticharact(self, u1, v1, u2, v2): #construction courbe tension vitesse
        v1 = (v1*(2*math.pi*self.diametre))/60
        v2 = (v2*(2*math.pi*self.diametre))/60   #transfo tr/min en m/s
        self.theta_pp = (self.acc / (self.entraxe / 2)) * (180 / math.pi) * 100
        self.acc = ((v1 - v2)/(u1 - u2))
        self.p = v1 - (u1*self.acc)
        self.u0 = -(self.p/self.acc)
        self.vCroisiere = self.acc*self.uvCroisiere + self.p
        self.wCroisiere = 7.68
        #print(self.theta_pp)
        #print('vC:', self.vCroisiere)
        #print('wC:', self.wCroisiere)
        #print('u0', self.u0)
        #print('acc:', self.acc)
        #print('p:', self.p)

    def profile_l(self,t):
        l_acc = (self.vCroisiere**2)/(2*self.acc)
        #print('l_acc:', l_acc)
        if (self.l/2) <= l_acc:
            t_bang = (self.l/(self.acc))**(1/2)
            self.t_total1 = 2*t_bang
            if t <= t_bang:
                self.l_evo = (self.acc/2)*(t**2)
            else:
                self.l_evo = -(self.acc/2)*((t - t_bang)**2) + (t_bang*self.acc)*(t - t_bang) + (self.l/2)
            #print('t bang:', t_bang)


        else :
            l_croisiere = self.l - ((self.vCroisiere ** 2) / self.acc)
            t_bang = self.vCroisiere/self.acc
            self.t_total1 = (2*t_bang) + (l_croisiere/self.vCroisiere)
            t_croisiere = l_croisiere/self.vCroisiere

            if t < t_bang:
                self.l_evo = (self.acc/2)*(t**2)

            if (t > t_bang) and (t < self.t_total1 - t_bang):
                self.l_evo = self.vCroisiere*(t - t_bang) + l_acc

            if t > (self.t_total1 - t_bang):
                self.l_evo = -(self.acc/2)*((t - (t_bang + t_croisiere))**2) + self.vCroisiere*(t - (t_bang + t_croisiere)) + l_croisiere + l_acc
        print('l evo:', self.l_evo)
        print('total1:', self.t_total1)

    def profile_theta(self,t):
        print('thetapp:', self.theta_pp)
        #print('wcroisiere:', self.wCroisiere)
        theta_acc = (self.wCroisiere**2) / (2*self.theta_pp)
        #print('theta acc:', theta_acc)
        t_croisiere = 0
        if abs(self.theta/2) <= theta_acc:
            if self.theta > 0:
                t_bang = (abs(self.theta) / self.theta_pp) ** (1/2)
                #print('theta:', self.theta)
                #print('thetapp:', self.theta_pp)
                #print('t bang:', t_bang)
                self.t_total2 = 2*t_bang
                if t <= t_bang:
                    self.theta_evo = (self.theta_pp / 2) * (t**2)
                else:
                    self.theta_evo = -(self.theta_pp / 2) * ((t - t_bang) ** 2) + (t_bang * self.theta_pp) * (t - t_bang) + (self.theta / 2)
                #print('t bang:', t_bang)
            else:
                t_bang = (abs(self.theta) / self.theta_pp) ** (1/2)
                #print('theta:', self.theta)
                #print('thetapp:', self.theta_pp)
                #print('t bang:', t_bang)
                self.t_total2 = 2*t_bang
                if t <= t_bang:
                    self.theta_evo = -(self.theta_pp / 2) * (t**2)
                else:
                    self.theta_evo = (self.theta_pp / 2) * ((t - t_bang) ** 2) + (t_bang * -self.theta_pp) * (t - t_bang) + (self.theta / 2)
                #print('t bang:', t_bang)

        else:
            theta_croisiere = abs(self.theta) - ((self.wCroisiere ** 2) / self.theta_pp)
            t_bang = self.wCroisiere / self.theta_pp
            self.t_total2 = (2 * t_bang) + (theta_croisiere / self.wCroisiere)
            t_croisiere = theta_croisiere / self.wCroisiere
            if self.theta > 0 :
                if t < t_bang:
                    self.theta_evo = (self.theta_pp / 2) * (t ** 2)

                if (t > t_bang) and (t < self.t_total2 - t_bang):
                    self.theta_evo = self.wCroisiere * (t - t_bang) + theta_acc

                if t > (self.t_total2 - t_bang):
                    self.theta_evo = -(self.theta_pp / 2) * ((t - (t_bang + t_croisiere)) ** 2) + self.wCroisiere * (
                            t - (t_bang + t_croisiere)) + theta_croisiere + theta_acc
            else:
                if t < t_bang:
                    self.theta_evo = (-self.theta_pp / 2) * (t ** 2)

                if (t > t_bang) and (t < self.t_total2 - t_bang):
                    self.theta_evo = -self.wCroisiere * (t - t_bang) - theta_acc

                if t > (self.t_total2 - t_bang):
                    self.theta_evo = (self.theta_pp / 2) * (
                            (t - (t_bang + t_croisiere)) ** 2) - self.wCroisiere * (
                                             t - (t_bang + t_croisiere)) - theta_croisiere - theta_acc
        print(2*t_bang + t_croisiere)
        print('theta evo:', self.theta_evo)
        print('total2:', self.t_total2)

    def PIDregulator(self, x_cible, y_cible):
        kp_l = 2.5
        ki_l = 0
        kd_l = 0

        kp_theta = 15
        ki_theta = 0
        kd_theta = 0

        if self.motorL.heading() > 180:
            heading = self.motorL.heading() - 360
        else :
            heading = self.motorL.heading()
        print('heading:', heading)


        self.theta_error = self.theta_evo - heading
        print('theta error:', self.theta_error)
        self.somme_theta_error += self.theta_error
        self.delta_theta_error = self.theta_error - self.last_theta_error
        self.last_theta_error = self.theta_error

        print('p:', kp_theta*self.theta_error)
        print('i:', ki_theta*self.somme_theta_error)
        print('d:', kd_theta*self.delta_theta_error)
        print('order:', kp_theta*self.theta_error + ki_theta*self.somme_theta_error + kd_theta*self.delta_theta_error)

        self.theta_order = kp_theta*self.theta_error + ki_theta*self.somme_theta_error + kd_theta*self.delta_theta_error



        self.l_error = self.l_evo - (self.l - ((x_cible - self.center[0])**2 + (y_cible - self.center[1])**2)**(1/2))
        print('error l:', self.l_error)
        self.somme_l_error += self.l_error
        self.delta_l_error = self.l_error - self.last_l_error
        self.last_l_error = self.l_error

        self.l_order = kp_l * self.l_error + ki_theta * self.somme_l_error + kd_theta * self.delta_l_error

        self.theta_order *= 6/255
        self.l_order *= 6/255

        self.l_order += self.u0
        self.theta_order += self.u0

        if self.l_order > self.uMax:
            self.l_order = self.uMax
        if self.l_order < -self.uMax:
            self.l_order = -self.uMax


        if self.theta_order > self.uMax:
            self.theta_order = self.uMax
        if self.theta_order < (-self.uMax):
            self.theta_order = -(self.uMax)

        print('cmd theta:', self.theta_order)
        print('cmd l:', self.l_order)

        self.speed_l = self.acc*self.l_order + self.p
        self.speed_theta = ((self.acc*self.theta_order + self.p)/((self.entraxe/2)))*(180/math.pi)
        #print('speed theta:', self.speed_theta)

        self.delta_speed_theta = self.speed_theta - self.last_speed_theta
        self.delta_speed_l = self.speed_l - self.last_speed_l
        #print('last speed old:', self.last_speed_theta)

        self.last_speed_theta = self.speed_theta
        self.last_speed_l = self.speed_l
        #print('last speed:', self.last_speed_theta)

        #if abs(self.theta_order) < abs(self.u0):
            #self.speed_theta = 0
        #else:
            #self.speed_theta = (((self.acc*self.theta_order) + self.p)/(self.entraxe/2))*(180/math.pi)
        #print('w:', self.speed_theta)

        #if abs(self.theta_order) < abs(self.u0):
            #self.speed_theta = 0
        #else:
            #self.speed_theta = (((self.acc*self.theta_order) + self.p)/(self.entraxe/2))*(180/math.pi)
        print('w:', self.speed_theta)
        print('v:', self.speed_l)



    def forreal(self, method):
        pass
        if self.error == 0:
            self.error = 2
            # self.error = random.uniform(-0.15,0.15)
        self.thp += self.error
        self.random += 1
        if self.random % 25 == 0:
            self.error2 = random.uniform(-2, 2)
        if 0 < self.random % 25 < 5:
            print('random')
            self.thp += self.error2


    def tourne1(self, dt):
        pass
        if self.theta < 0 :
            self.motorR.circle(-self.entraxe/2, -self.speed_theta*dt)
            self.motorL.circle(self.entraxe/2, self.speed_theta*dt)
        else :
            self.motorR.circle(-self.entraxe / 2, self.speed_theta*dt)
            self.motorL.circle(self.entraxe / 2, -self.speed_theta*dt)
        #print('heading:', self.motorR.heading())

    def tourne(self, dt):
        self.motorR.circle(-self.entraxe/2, -self.speed_theta*dt)
        self.motorL.circle(self.entraxe/2, self.speed_theta*dt)
        #print('heading:', self.motorR.heading())


    def roule(self, dt):
        self.motorR.fd(self.speed_l*dt)
        self.motorL.fd(self.speed_l*dt)
        xR, yR = self.motorR.position()
        xL, yL = self.motorL.position()
        x = (xR + xL) / 2
        y = (yR + yL) / 2
        self.center = (x, y)
        print('center:', self.center)

    def runmotor(self, x_cible, y_cible, u1, u2, v1, v2):
        self.odometry()
        self.consigne(x_cible, y_cible)
        self.staticharact(u1, v1, u2, v2)
        self.profile_theta(0)
        self.profile_l(0)
        dt = 0
        ti = time.time()
        dt1 = 0
        dt2 = 0
        print('tt2:', self.t_total2)

        while 1 == 1 :

            tf = time.time()
            dt = tf - ti
            tf = ti
            ti = time.time()
            dt1 += dt
            dt2 += dt
            if dt2 > self.t_total2:
                dt2 = self.t_total2
            self.profile_theta(dt2)
            self.profile_l(0)
            self.PIDregulator(x_cible, y_cible)
            self.tourne(dt)
            self.roule(dt)
            print('dt:', dt)
            print('dt1:', dt1)
            if dt != 0:
                print('var theta:', self.delta_speed_theta/dt)

        dt1 = 0
        dt2 = 0

        while dt2 < self.t_total1 or self.speed_l > 0.001:
            tf = time.time()
            dt = tf - ti
            tf = ti
            ti = time.time()
            dt1 += dt
            dt2 += dt
            if dt1 > self.t_total1:
                dt1 = self.t_total1
            self.profile_l(dt1)
            self.PIDregulator(x_cible, y_cible)
            self.tourne(dt)
            self.roule(dt)
            print('dt:', dt)
            print('dt1:', dt2)











        #print((self.motorL.pos()[0] + self.motorR.pos()[0]) / 2, (self.motorL.pos()[1] + self.motorR.pos()[1]) / 2)


def main():
    test1 = Motor("red", 20)
    test1.odometry()
    test1.staticharact(6,160,3,60)
    test1.consigne(-10, 10)
    test1.runmotor(-10,10,3,6,60,160)











    input()

main()



