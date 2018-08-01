import pygame as pg
import queue
from environment import *
from settings import *
from sprites import *

class TileMenu():
    def __init__(self, game, x, y, tile):
        options = ["Move"]
        self.menuItems = []
        self.game = game
        self.startX = x
        self.tile = tile
        self.startY = y
        count = 0
        for option in options:
            self.menuItems.append(Option(self.startX, self.startY + (count * (OPTION_HEIGHT - 1)), self, option, self.game, count, True))
            count += 1
        self.addListeners()

    def itemSelected(self, item):
        if item.id == 0:
            self.game.selectedEntity.moveTo(self.tile)
            self.game.selectedEntity.unselectEntity()
            self.game.selectedEntity = None
            self.destroy()

    def addListeners(self):
        for item in self.menuItems:
            self.game.widgets.append(item)

    def destroy(self):
        for item in self.menuItems:
            item.kill()
            self.game.widget_sprites.remove(item)
            self.game.widgets.remove(item)

class ShrineMenu():
    def __init__(self, game, x, y, shrine, entity):
        options = ["Capture"]
        self.menuItems = []
        self.game = game
        self.startX = x
        self.startY = y
        self.entity = entity
        self.shrine = shrine
        if(not entity.hasMoved and not shrine.occupied()):
            self.action = True
        else:
            self.action = False

        count = 0
        for option in options:
            self.menuItems.append(Option(self.startX, self.startY + (count * (OPTION_HEIGHT - 1)), self, option, self.game, count, self.action))
            count += 1
        self.addListeners()

    def itemSelected(self, item):
        if item.id == 0:
            self.shrine.capture(self.entity)
            self.entity.hasMoved = True
            self.destroy()

    def addListeners(self):
        for item in self.menuItems:
            self.game.widgets.append(item)

    def destroy(self):
        for item in self.menuItems:
            item.kill()
            self.game.widget_sprites.remove(item)
            self.game.widgets.remove(item)


class AttackMenu():
    def __init__(self, game, x, y, entity, direction):
        options = ["Fire"]
        self.menuItems = []
        self.game = game
        self.startX = x
        self.startY = y
        self.entity = entity
        self.direction = direction
        self.action = True
        count = 0
        for option in options:
            self.menuItems.append(Option(self.startX, self.startY + (count * (OPTION_HEIGHT - 1)), self, option, self.game, count, self.action))
            count += 1
        self.addListeners()

    def itemSelected(self, item):
        if item.id == 0:
            Bullet(self.game, None, self.entity, BLACK, (self.entity.x + GRID_START_X + TILESIZE/2, self.entity.y + GRID_START_Y + TILESIZE/2), (self.direction[0],self.direction[1]), 10, 1)
            self.entity.endShot = True
            self.destroy()

    def addListeners(self):
        for item in self.menuItems:
            self.game.widgets.append(item)

    def destroy(self):
        for item in self.menuItems:
            item.kill()
            self.game.widget_sprites.remove(item)
            self.game.widgets.remove(item)




class Option(pg.sprite.Sprite):
        def __init__(self, x, y, menu, text, game, id, clickable):
            self.id = id
            self.groups = game.widget_sprites
            self.game = game
            pg.sprite.Sprite.__init__(self, self.groups)
            self.menu = menu
            self.x = x
            self.y = y
            self.clickable = clickable
            font= pg.font.Font(None, 14)
            self.image = pg.Surface((OPTION_WIDTH, OPTION_HEIGHT))
            self.image.set_alpha(230)
            self.image.fill(OPTION_DEFAULT)
            pg.draw.rect(self.image, BLACK, (0, 0, OPTION_WIDTH, OPTION_HEIGHT), 1)
            self.rect = self.image.get_rect()
            if(clickable):
                self.optionText = font.render(text, 1, (0,0,0))
            else:
                self.optionText = font.render(text, 1, (50,50,50))

            self.image.blit(self.optionText, (5, 7))

        def update(self):
            self.rect.x = (self.x)
            self.rect.y = (self.y)
            (mouseX, mouseY) = pg.mouse.get_pos()
            if(self.isMouseOver(mouseX, mouseY)):
                self.image.fill(OPTION_HIGHLIGHT)
                self.image.blit(self.optionText, (5, 7))
                pg.draw.rect(self.image, BLACK, (0, 0, OPTION_WIDTH, OPTION_HEIGHT), 1)
            else:
                self.image.fill(OPTION_DEFAULT)
                self.image.blit(self.optionText, (5, 7))
                pg.draw.rect(self.image, BLACK, (0, 0, OPTION_WIDTH, OPTION_HEIGHT), 1)


        def events(self, event):
                if event.type == pg.MOUSEBUTTONUP:
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    if event.button == 1:
                        if self.isMouseOver(mouseX, mouseY):
                            if(self.clickable):
                                self.menu.itemSelected(self)
                        else:
                            self.menu.destroy()
                    if event.button == 3:
                        self.menu.destroy()

        def isMouseOver(self, x, y):
            if(x > self.x) and (x < (self.x + OPTION_WIDTH)) and (y > self.y) and (y < (self.y + OPTION_HEIGHT)):
                return True
            else:
                return False


class Button(pg.sprite.Sprite):
    def __init__(self, game, text, id, x, y, width = BUTTON_WIDTH):
        self.id = id
        self.game = game
        self.groups = game.widget_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.x = x
        self.y = y
        self.text = text
        self.width = width
        self.isSelected = False
        font= pg.font.Font(None, 24)
        self.image = pg.Surface((width, BUTTON_HEIGHT))
        self.image.fill(OPTION_DEFAULT)
        pg.draw.rect(self.image, BLACK, (0, 0, BUTTON_WIDTH, BUTTON_HEIGHT), 1)
        self.rect = self.image.get_rect()
        self.optionText = font.render(text, 1, (0,0,0))
        self.fontWidth, self.fontHeight = font.size(text)
        self.image.blit(self.optionText, (BUTTON_WIDTH - self.fontWidth, BUTTON_HEIGHT - self.fontHeight))
        self.game.widgets.append(self)

    def update(self):
        self.rect.x = (self.x)
        self.rect.y = (self.y)
        (mouseX, mouseY) = pg.mouse.get_pos()
        if(self.isSelected):
            self.image.fill(OPTION_SELECTED)
        else:
            if(self.isMouseOver(mouseX, mouseY)):
                self.image.fill(OPTION_HIGHLIGHT)
            else:
                self.image.fill(OPTION_DEFAULT)

        self.image.blit(self.optionText, (self.width/2 - self.fontWidth/2, BUTTON_HEIGHT/2 - self.fontHeight/2))
        pg.draw.rect(self.image, BLACK, (0, 0, self.width, BUTTON_HEIGHT), 1)


    def events(self, event):
        if event.type == pg.MOUSEBUTTONUP:

            (mouseX, mouseY) = pg.mouse.get_pos()
            if event.button == 1:
                if self.isMouseOver(mouseX, mouseY):
                    self.game.buttonPressed(self)

    def destroy(self):
        self.game.widget_sprites.remove(self)
        self.game.widgets.remove(self)
        self.kill()

    def isMouseOver(self, x, y):
        if(x > self.x) and (x < (self.x + self.width)) and (y > self.y) and (y < (self.y + BUTTON_HEIGHT)):
            return True
        else:
            return False
