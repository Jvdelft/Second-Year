import os
import pygame
from math import tan, radians, degrees, copysign, cos, sin
from pygame.math import Vector2
import time
import random

cylindre_position = [(346,244),(346,501),(545,244),(545,501),(743,244),(743,501),(942,244),(942,501),(1141,244),(1141,501),(1337,244),(1337,501)]

def detection(cylindre_liste, capteur_liste):
        droite, gauche = False, False
        for elem1 in cylindre_liste:

            if pygame.Rect.colliderect(elem1, capteur_liste[0]):
                droite = True
            if pygame.Rect.colliderect(elem1, capteur_liste[1]):
                gauche = True
        return (droite,gauche)


def tri(liste, cylindre_position, cote):
        for elem in liste :
            if type(elem[0]) == int:
                if elem[0] < 285:
                    elem[0] = "petit"
                elif elem[0] < 320 and elem[0] > 285:
                    elem[0] = "moyen"
                else:
                    elem[0] = "grand"

            for i in range(6):
                if cote == "left" and type(elem[1]) != int:
                    if cylindre_position[2*i][0] +15 > elem[1][0] and cylindre_position[2*i][0] - 15 < elem[1][0]:
                        elem[1] = 2*i
                if cote == "right" and type(elem[1]) != int:
                    if cylindre_position[2*i+1][0] + 15 > elem[1][0] and cylindre_position[2*i+1][0] - 15 < elem[1][0]:
                        elem[1] = 2*i+1
        return liste



class Car:
    def __init__(self, x, y, angle=0.0, length=4, max_steering=30, max_acceleration=5.0):
        self.position = Vector2(x, y)
        self.velocity = Vector2(0.0, 0.0)
        self.angle = angle
        self.length = length
        self.max_acceleration = max_acceleration
        self.max_steering = max_steering
        self.max_velocity = 2
        self.brake_deceleration = 1
        self.free_deceleration = 0.2

        self.acceleration = 0.0
        self.steering = 0.0

    def update(self, dt):
        self.velocity += (self.acceleration * dt, 0)
        self.velocity.x = max(-self.max_velocity, min(self.velocity.x, self.max_velocity))

        if self.steering:
            turning_radius = self.length / tan(radians(self.steering))
            angular_velocity = self.velocity.x / turning_radius
        else:
            angular_velocity = 0

        self.position += self.velocity.rotate(-self.angle) * dt
        self.angle += degrees(angular_velocity) * dt


class Simulation:
    def __init__(self):
        pygame.init()
        screen = pygame.display.set_mode((1500,750))
        self.screen = screen
        self.fond = pygame.image.load("table projet.png").convert()
        screen.blit(self.fond,(0,0))
        self.clock = pygame.time.Clock()
        self.ticks = 60
        self.exit = False

    
    def run(self):
        current_dir = os.path.dirname(os.path.abspath(__file__))
        image_path = os.path.join(current_dir, "rond.png")
        car_image = pygame.image.load(image_path).convert_alpha()
        car = Car(10,10)
        ppu = 16
        
        i1, i2, i3, i4, i5, i6, i7 = True, True, True, True, True, True, True
        lignes = [345,545,744,942,1140,1339]
        cylindres_position = [(346,244),(346,501),(545,244),(545,501),(743,244),(743,501),(942,244),(942,501),(1141,244),(1141,501),(1337,244),(1337,501)]
        ralenti = False
        random.shuffle(cylindres_position)
        nb_tick_droite = 0
        nb_tick_gauche = 0
        detected_ticks_right = []
        detected_ticks_left = []
        angle_tourne = 0
        
        while not self.exit:
            dt = self.clock.get_time()/1000
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.exit = True
                if event.type == pygame.MOUSEBUTTONDOWN:
                    print(Vector2(event.pos))

            if not i5:
                for elem in lignes:
                    if (elem + rect.width/2 + 7 > car.position[0] * ppu + rect.width/2 and elem - 19 < car.position[0] * ppu + rect.width/2):
                        if car.velocity.x > 0.5:
                            car.max_acceleration = 0.6
                            car.acceleration -= 1000 *dt




            if car.position[0] < 20 and i1:
                car.acceleration += 1 * dt

            elif car.position[1] < 18 and i2:
                i1 = False
                if car.position[1]  < 16.3:
                    car.steering -= 30 * dt
                else:
                    car.steering = 0
                    car.acceleration += 0.5 * dt
                car.steering = max(-car.max_steering, min(car.steering, car.max_steering))

            elif car.position[0] < 35.1 and i3:
                i2 = False
                car.steering += 30 * dt
                car.steering = max(-car.max_steering, min(car.steering, car.max_steering))

            elif car.position[0] > 15 and i4:
                car.steering = 0
                i3 = False
                car.acceleration -= 5 * dt
                car.acceleration = max(-car.max_acceleration, min(car.acceleration, car.max_acceleration))
            elif car.position[0] < 15 and i5:
                i4 = False
                car.acceleration += 5 * dt
                car.acceleration = max(-car.max_acceleration, min(car.acceleration, car.max_acceleration))

            elif car.position[0] < 85 and i6:
                i5 = False
                car.acceleration += 1 * dt
                car.acceleration = max(-car.max_acceleration, min(car.acceleration, car.max_acceleration))

            elif car.position[0] > 70 and not i5:
                i6 = False
                if car.position[0] > 80 and i7:
                    car.acceleration -= 1 * dt
                    car.acceleration = max(-car.max_acceleration, min(car.acceleration, car.max_acceleration))
                elif car.position[0] < 83 and (10 in detected_ticks_left[len(detected_ticks_left)-1] and detected_ticks_left[len(detected_ticks_left)-1][0] != 'moyen'):
                    i7 = False
                    if car.velocity.x < 2 and car.position[0] < 85 :
                        car.acceleration += 3*dt
                        car.acceleration = max(-car.max_acceleration, min(car.acceleration, car.max_acceleration))
                        #print(angle_tourne)
                    #elif angle_tourne < 90:
                     #   print("prout")
                      #  car.steering += 30*dt
                       # angle_tourne += car.angle
                    #car.steering = max(-car.max_steering, min(car.steering, car.max_steering))
            elif car.position[0] < 85 and not i7:
                car.steering += 30 * dt
                car.steering = max(-car.max_steering, min(car.steering, car.max_steering))
                
                    
            cylindre_1 = pygame.draw.circle(self.screen, (0,0,255), cylindres_position[0], 12)
            cylindre_2 = pygame.draw.circle(self.screen, (0,0,255), cylindres_position[1], 12)
            cylindre_3 = pygame.draw.circle(self.screen, (255,0,0), cylindres_position[2], 16)
            cylindre_4 = pygame.draw.circle(self.screen, (255,0,0), cylindres_position[3], 16)
            cylindre_5 = pygame.draw.circle(self.screen, (0,255,0), cylindres_position[4], 21)
            cylindre_6 = pygame.draw.circle(self.screen, (0,255,0), cylindres_position[5], 21)

            liste_cylindres = [cylindre_1, cylindre_2, cylindre_3, cylindre_4, cylindre_5, cylindre_6]
                                
            rotation = pygame.transform.rotate(car_image, car.angle)
            rect = rotation.get_rect()
            car.update(dt)

            a =  (car.position*ppu + (0,rect.height/2))[0] + 400 * sin(radians(car.angle))
            b = (car.position*ppu + (0,rect.height/2))[1] + 400 * cos(radians(car.angle))
            c = (car.position*ppu - (0,rect.height/2))[0] - 400 * sin(radians(car.angle))
            d = (car.position*ppu - (0,rect.height/2))[1] - 400 * cos(radians(car.angle))
            

            capteur_1 = pygame.draw.line(self.screen, (0,0,0), car.position*ppu + (0,rect.height/2), (a,b),2)
            capteur_2 = pygame.draw.line(self.screen, (0,0,0), car.position*ppu - (0,rect.height/2), (c,d),2)

            liste_capteurs = [capteur_1, capteur_2]
            
            detecte_droite, detecte_gauche = detection(liste_cylindres, liste_capteurs)

            if i6:
                if detecte_droite and not i4:
                    nb_tick_droite += 1
                elif nb_tick_droite > 0:
                    detected_ticks_right.append([nb_tick_droite,car.position*ppu -(10,0)])
                    nb_tick_droite = 0
                
                if detecte_gauche and not i4:
                    nb_tick_gauche += 1
                elif nb_tick_gauche > 0:
                    detected_ticks_left.append([nb_tick_gauche,car.position*ppu - (10,0)])
                    nb_tick_gauche = 0

            detected_ticks_left = tri(detected_ticks_left, cylindre_position, "left")
            detected_ticks_right = tri(detected_ticks_right, cylindre_position ,"right")
            if len(detected_ticks_left) !=0:
                    print(detected_ticks_left)
            if len(detected_ticks_right) != 0:
                    print(detected_ticks_right)
            

            self.screen.blit(rotation, car.position * ppu - (rect.width/2,rect.height/2))
            pygame.display.flip()
            self.screen.blit(self.fond,(0,0))
            self.clock.tick(self.ticks)
            
        pygame.quit()
            
simulation = Simulation()
simulation.run()
