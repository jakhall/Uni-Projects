import pygame as pg
import queue
from environment import *
from settings import *

class Entity(pg.sprite.Sprite):
    def __init__(self, game, x, y, team, health):
        self.groups = game.entity_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.isSelected = False
        self.game = game
        self.range = 4
        self.moveableTiles = []
        self.team = team
        self.attackRows = []
        self.health = health
        self.selectedAttackRow = None
        if(self.team < 1):
            self.color = GREEN
        elif(self.team < 2):
            self.color = BLUE
        elif(self.team < 3):
            self.color = RED
        self.hasMoved = False
        self.endShot = False
        self.moveActivated = False
        self.image = pg.Surface((TILESIZE, TILESIZE))
        self.image.fill(WHITE)
        self.image.set_colorkey(WHITE)
        self.rect = self.image.get_rect()
        pg.draw.circle(self.image, self.color, (int(self.rect.x + TILESIZE/2), int(self.rect.y + TILESIZE/2)), 10)
        self.moveQueue = queue.Queue()
        self.speed = TILESIZE/4
        self.isMoving = False
        self.x = x * TILESIZE
        self.y = y * TILESIZE
        self.gridX = x
        self.gridY = y
        self.tile = None
        self.changeTile(self.gridX, self.gridY)
        self.healthBar = HealthBar(self.game, self)

    def move(self):
        if(self.isMoving):
            if self.x != self.newX:
                self.x += self.dx * self.speed
            elif self.y != self.newY:
                self.y += self.dy * self.speed
            else:
                self.isMoving = False

    def nearestEnemy(self):
        nearest = None
        for entity in self.game.entityList:
            if(entity.team != self.team):
                if(nearest is None):
                    nearest = (entity, self.tile.getH(entity.tile.gridX, entity.tile.gridY))
                else:
                    distance = self.tile.getH(entity.tile.gridX, entity.tile.gridY)
                    if(nearest[1] > distance):
                        nearest = (entity, distance)
        if nearest is not None:
            return nearest[0]
        else:
            return None


    def selectEntity(self):
        self.isSelected = True

    def changeTile(self, x, y):
        if(self.tile is not None):
            self.tile.isOccupied = False
            self.tile.isPassable = True
            self.tile.entityList.remove(self)
        self.tile = self.game.grid.tiles[x][y]
        self.tile.isOccupied = True
        self.tile.isPassable = False
        self.tile.entityList.append(self)

    def unselectEntity(self):
        self.isSelected = False
        self.deleteMoveOptions()
        self.unhighlightAttack()
        if self.selectedAttackRow is not None:
            self.unhighlightAttackRow(self.selectedAttackRow)
            self.selectedAttackRow = None

    def calculateAttack(self):
        directions = [-1, 1]

        attackTilesLeft = ([], [-1, 0], None)
        attackTilesRight = ([], [1, 0], None)
        for d in directions:
            gridX = self.tile.gridX + d
            gridY = self.tile.gridY
            selectedTile = self.game.grid.getGridTile(gridX, gridY)
            if(selectedTile is not None):
                if(selectedTile.isPassable == False):
                    if(len(selectedTile.entityList) > 0):
                        if(d > 0):
                            attackTilesRight = (attackTilesRight[0], attackTilesRight[1], selectedTile.entityList[0])
                        else:
                            attackTilesLeft = (attackTilesLeft[0], attackTilesLeft[1], selectedTile.entityList[0])
            while(selectedTile is not None):
                if(d > 0):
                    attackTilesRight[0].append(selectedTile)
                else:
                    attackTilesLeft[0].append(selectedTile)
                gridX += d
                selectedTile = self.game.grid.getGridTile(gridX, gridY)
                if(selectedTile is not None):
                    if(selectedTile.isPassable == False):
                        if(len(selectedTile.entityList) > 0):
                            if(d > 0):
                                attackTilesRight = (attackTilesRight[0], attackTilesRight[1], selectedTile.entityList[0])
                            else:
                                attackTilesLeft = (attackTilesLeft[0], attackTilesLeft[1], selectedTile.entityList[0])

                        selectedTile = None

        attackTilesUp = ([], [0, -1], None)
        attackTilesDown = ([], [0, 1], None)
        for d in directions:
            gridX = self.tile.gridX
            gridY = self.tile.gridY + d
            selectedTile = self.game.grid.getGridTile(gridX, gridY)
            if(selectedTile is not None):
                if(selectedTile.isPassable == False):
                    if(len(selectedTile.entityList) > 0):
                        if(d > 0):
                            attackTilesRight = (attackTilesRight[0], attackTilesRight[1], selectedTile.entityList[0])
                            attackTilesRight[0].append(selectedTile)
                        else:
                            attackTilesLeft = (attackTilesLeft[0], attackTilesLeft[1], selectedTile.entityList[0])
                            attackTilesLeft[0].append(selectedTile)
            while(selectedTile is not None):
                if(d > 0):
                    attackTilesDown[0].append(selectedTile)
                else:
                    attackTilesUp[0].append(selectedTile)
                gridY += d
                selectedTile = self.game.grid.getGridTile(gridX, gridY)
                if(selectedTile is not None):
                    if(selectedTile.isPassable == False):
                        if(len(selectedTile.entityList) > 0):
                            if(d > 0):
                                attackTilesDown = (attackTilesDown[0], attackTilesDown[1], selectedTile.entityList[0])
                                attackTilesDown[0].append(selectedTile)
                            else:
                                attackTilesUp = (attackTilesUp[0], attackTilesUp[1], selectedTile.entityList[0])
                                attackTilesUp[0].append(selectedTile)
                        selectedTile = None

        self.attackRows = [attackTilesLeft, attackTilesRight, attackTilesUp, attackTilesDown]

        return self.attackRows


    def highlightAttack(self):
        if(not self.endShot):
            if len(self.attackRows) > 0:
                self.unhighlightAttack()
            self.calculateAttack()
            for row in self.attackRows:
                for tile in row[0]:
                    tile.setHighlight(Highlight(self.game, RED_HIGHLIGHT, 80))

    def unhighlightAttack(self):
        for row in self.attackRows:
            for tile in row[0]:
                tile.destroyHighlight()
        self.attackRows = []

    def highlightAttackRow(self, row):
        for tile in row[0]:
            tile.setSecondHighlight(Highlight(self.game, RED, 80))

    def unhighlightAttackRow(self, row):
        for tile in row[0]:
            tile.destroySecondHighlight()

    def calculateOptions(self):
        if(not self.hasMoved):
            tiles = self.game.grid.getTilesInRange(self.range, self.tile, 0)
            for tile in tiles:
                path = self.game.findAStarPath(self.tile, tile, False)
                if(path is not None):
                    if len(path) <= self.range:
                        self.moveableTiles.append(tile)
        return self.moveableTiles


    def moveOptions(self):
        if len(self.moveableTiles) > 0:
            self.deleteMoveOptions()
        self.calculateOptions()
        for tile in self.moveableTiles:
            tile.setHighlight(Highlight(self.game, DEFAULT_HIGHLIGHT, 80))

    def deleteMoveOptions(self):
        for tile in self.moveableTiles:
            tile.destroyHighlight()
        self.moveableTiles = []

    def moveTile(self):
        self.isMoving = True
        #print(repr(self.gridX) + " " + repr(self.gridY))
        (self.dx, self.dy) = self.moveQueue.get()
        self.gridX += self.dx
        self.gridY += self.dy
        self.changeTile(self.gridX, self.gridY)
        self.newX = self.x + (self.dx*TILESIZE)
        self.newY = self.y + (self.dy*TILESIZE)


    def moveToTile(self, tile):
        (tileX, tileY) = tile.getGridPosition()
        changeX = tileX - self.gridX
        changeY = tileY - self.gridY
        directionX = self.setDirection(changeX)
        directionY = self.setDirection(changeY)
        for x in range(0, abs(changeX)):
            self.moveQueue.put((directionX, 0))

        for y in range(0, abs(changeY)):
            self.moveQueue.put((0, directionY))

    def movePath(self, path):
        (previousX, previousY) = self.tile.getGridPosition()
        self.moveActivated = True
        for tile in path:
            directionX = self.setDirection(tile.gridX - previousX)
            directionY = self.setDirection(tile.gridY - previousY)
            self.moveQueue.put((directionX, directionY))
            previousX = tile.gridX
            previousY = tile.gridY

    def moveTo(self, endTile):
        path = self.game.findAStarPath(self.tile, endTile, False)
        if path is not None:
            self.movePath(path)
        else:
            print("None")

    def update(self):
        if(self.isSelected):
            self.image = pg.Surface((TILESIZE, TILESIZE))
            if(self.game.phase == 2 and self.game.currentTeam != self.team):
                self.image.fill(RED_HIGHLIGHT)
            else:
                self.image.fill(LIGHTGREEN)
            self.rect = self.image.get_rect()
            pg.draw.rect(self.image, CAPTUREBAR, (0, 0, TILESIZE, TILESIZE), 2)
            pg.draw.circle(self.image, self.color, (int(round(self.rect.x + TILESIZE/2)), int(round(self.rect.y + TILESIZE/2))), 9)
        else:
            self.image = pg.Surface((TILESIZE, TILESIZE))
            self.image.fill(WHITE)
            self.image.set_colorkey(WHITE)
            self.rect = self.image.get_rect()
            pg.draw.circle(self.image, self.color, (int(round(self.rect.x + TILESIZE/2)), int(round(self.rect.y + TILESIZE/2))), 9)

        self.rect.x = (self.x + GRID_START_X)
        self.rect.y = (self.y + GRID_START_Y)
        if self.isMoving:
            self.move()
        elif not self.moveQueue.empty():
            self.moveTile()
        elif self.moveActivated:
            self.hasMoved = True

        if(self.isSelected and self.game.phase == 2):
            for row in self.attackRows:
                check = False
                for tile in row[0]:
                    (mouseX, mouseY) = pg.mouse.get_pos()
                    if(tile.isMouseOver(mouseX, mouseY)):
                        check = True
                if check == True:
                    if(row != self.selectedAttackRow):
                        self.highlightAttackRow(row)
                        self.selectedAttackRow = row
                if self.selectedAttackRow is not None:
                    if check == False and row == self.selectedAttackRow:
                        self.unhighlightAttackRow(self.selectedAttackRow)
                        self.selectedAttackRow = None

    def shrineDistances(self):
        distances = []
        for item in self.game.items:
            if isinstance(item, Shrine):
                distance = self.tile.getEuclidean(item.tile.gridX, item.tile.gridY)
                distances.append((item, distance))

        return distances

    def setDirection(self, change):
        direction = 0
        if change > 0:
            direction = 1
        elif change < 0:
            direction = -1

        return direction

    def getH(self, goal):
        return self.tile.getH(goal.gridX, goal.gridY)

    def isMouseOver(self, x, y):
        tx = self.x + GRID_START_X
        ty = self.y + GRID_START_Y
        if(x > tx) and (x < (tx + TILESIZE)) and (y > ty) and (y < (ty + TILESIZE)):
            return True
        else:
            return False

    def isColliding(self, x, y):
        tx = self.x + GRID_START_X
        ty = self.y + GRID_START_Y
        if(x > tx) and (x < (tx + TILESIZE)) and (y > ty) and (y < (ty + TILESIZE)):
            return True
        else:
            return False

    def hit(self, damage):
        self.health -= damage
        if(self.health <= 0):
            self.destroy()

    def destroy(self):
        self.game.entity_sprites.remove(self)
        self.game.entityList.remove(self)
        self.tile.isPassable = True
        self.tile.isOccupied = False
        self.tile.entityList.remove(self)
        self.kill()

class Label(pg.sprite.Sprite):
    def __init__(self, game, text, size, x, y):
        self.groups = game.widget_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.game.widgets.append(self)
        self.text = text
        self.x = x
        self.y = y
        self.font= pg.font.Font(None, size)
        self.image = self.font.render(text, 1, (0, 0, 0))
        self.rect = self.image.get_rect()
        self.fontWidth, self.fontHeight = self.font.size(text)
        #self.image.blit(self.image, (BUTTON_WIDTH/2 - self.fontWidth, BUTTON_HEIGHT/2 - self.fontHeight))
        self.rect.y = self.y + 10

    def update(self):
        self.rect.x = self.x - self.fontWidth
        phase = self.game.phase
        text = ""
        if(phase < 1):
            text = "Drawing"
        elif(phase < 2):
            text = "Moving"
        elif(phase < 3):
            text = "Attacking"
        else:
            text = "Enemy"
        self.image = self.font.render("Phase: " + text, 1, (0, 0, 0))

    def destroy(self):
        self.game.widget_sprites.remove(self)
        self.game.widgets.remove(self)
        self.kill()

    def events(self, event):
        pass

class Text(pg.sprite.Sprite):
    def __init__(self, game, text, size, x, y):
        self.groups = game.widget_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.game.widgets.append(self)
        self.text = text
        self.x = x
        self.y = y
        self.font= pg.font.Font(None, size)
        self.image = self.font.render(text, 1, (0, 0, 0))
        self.rect = self.image.get_rect()
        self.fontWidth, self.fontHeight = self.font.size(text)
        self.rect.y = self.y
        self.rect.x = self.x

    def events(self, event):
        pass

    def destroy(self):
        self.game.widget_sprites.remove(self)
        self.game.widgets.remove(self)
        self.kill()

class Shrine(pg.sprite.Sprite):
    def __init__(self, game, isPassable, tile):
        self.isPassable = isPassable
        self.groups = game.item_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.tile = tile
        self.isSelected = False
        self.owner = 0
        self.color = GREEN
        self.captureTime = 3
        self.capturePercentage = 0
        self.beingCaptured = False
        self.capturingTeam = None
        self.highlightColor = DEFAULT_HIGHLIGHT
        self.image = pg.Surface((TILESIZE, TILESIZE))
        self.image.fill(self.color)
        self.rect = self.image.get_rect()

        self.range = 2
        self.captureBar = None

    def setPosition(self, x, y):
        self.x = x
        self.y = y
        self.rect.x = x
        self.rect.y = y
        self.tile = self.game.grid.getTile(x, y)
        self.tilesInRange = self.game.grid.getTilesInRange(self.range, self.tile, 1)

    def select(self):
        self.isSelected = True
        self.highlight()

    def deselect(self):
        self.isSelected = False
        self.unHighlight()
        #self.image = pg.Surface((TILESIZE, TILESIZE))
        #self.image.fill(WHITE)
        #self.image.set_colorkey(WHITE)
        #self.rect = self.image.get_rect()
        #self.rect.x = self.x
        #self.rect.y = self.y
        #pg.draw.circle(self.image, self.color, (int(TILESIZE/2), int(TILESIZE/2)), 9)

    def highlight(self):
        for tile in self.tilesInRange:
            tile.setHighlight(Highlight(self.game, self.highlightColor, 80))

    def unHighlight(self):
        for tile in self.tilesInRange:
            tile.destroyHighlight()

    def getAllInRange(self):
        inRange = []
        for tile in self.tilesInRange:
            for entity in tile.entityList:
                inRange.append(entity)
        return inRange

    def occupied(self):
        inRange = self.getAllInRange()
        for entity in inRange:
            if(entity.team == self.owner):
                return True
        return False

    def capture(self, entity):
        if(entity.team < 1):
            self.highlightColor = DEFAULT_HIGHLIGHT
            self.color = GREEN
        elif(entity.team < 2):
            self.highlightColor = BLUE_HIGHLIGHT
            self.color = BLUE
        elif(entity.team < 3):
            self.highlightColor = RED_HIGHLIGHT
            self.color = RED
        self.beingCaptured = True
        self.captureBar = CaptureBar(self.game, self, self.color)
        self.capturingTeam = entity.team

    def isMouseOver(self, x, y):
        if(x > self.x) and (x < (self.x + TILESIZE)) and (y > self.y) and (y < (self.y + TILESIZE)):
            return True
        else:
            return False

    def update(self):
        if(self.beingCaptured):
            self.capturePercentage += self.captureTime
            if(self.capturePercentage >= 200):
                self.image.fill(self.color)
                self.beingCaptured = False
                self.owner = self.capturingTeam
                self.capturingTeam = None
                self.captureBar = None
        if(self.isSelected):
            pg.draw.rect(self.image, WHITE, (0, 0, TILESIZE, TILESIZE), 2)
        else:
            pg.draw.rect(self.image, BLACK, (0, 0, TILESIZE, TILESIZE), 2)

    def isColliding(self, x, y):
        if(x > self.x) and (x < (self.x + TILESIZE)) and (y > self.y) and (y < (self.y + TILESIZE)):
            return True
        else:
            return False

    def hit(self, damage):
        pass

class Obstacle(pg.sprite.Sprite):
    def __init__(self, game, isPassable, tile):
        self.isPassable = isPassable
        self.groups = game.item_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.tile = tile
        self.image = pg.Surface((TILESIZE, TILESIZE))
        self.image.fill(DARKGRAY)
        self.rect = self.image.get_rect()
        self.health = 1

    def setPosition(self, x, y):
        self.rect.x = x
        self.rect.y = y

    def destroy(self):
        self.tile.destroyItem()

    def isColliding(self, x, y):
        if(x > self.rect.x) and (x < (self.rect.x + TILESIZE)) and (y > self.rect.y) and (y < (self.rect.y + TILESIZE)):
            return True
        else:
            return False

    def hit(self, damage):
        self.health -= damage
        if(self.health <= 0):
            self.destroy()

class Highlight(pg.sprite.Sprite):
    def __init__(self, game, color, opacity):
        self.color = color
        self.groups = game.highlight_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.image = pg.Surface((TILESIZE, TILESIZE))
        self.image.fill(color)
        self.image.set_alpha(opacity)
        self.rect = self.image.get_rect()
    def setPosition(self, x, y):
        self.rect.x = x
        self.rect.y = y


class HealthBar(pg.sprite.Sprite):
    def __init__(self, game, entity):
        self.groups = game.widget_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.units = entity.health
        self.game = game
        self.game.widgets.append(self)
        self.entity = entity
        self.unitWidth = 8
        self.unitGap = 1
        self.width = ((self.unitWidth + self.unitGap) * self.units) - self.unitGap
        self.height = 10
        self.drawBar()

    def update(self):
        if(self.units != self.entity.health):
            self.units = self.entity.health
            if self.units < 1:
                self.destroy()
            else:
                self.width = ((self.unitWidth + self.unitGap) * self.units) - self.unitGap
                self.drawBar()
        self.rect.x = self.entity.x + GRID_START_X + TILESIZE/2 - self.width/2
        self.rect.y = self.entity.y + GRID_START_Y - 15


    def drawBar(self):
        self.image = pg.Surface((self.width, self.height))
        self.image.fill(WHITE)
        self.image.set_colorkey(WHITE)
        currentX = 0
        for unit in range(0, self.units):
            pg.draw.rect(self.image, HEALTHBAR, [currentX, 0, self.unitWidth, self.height])
            #pg.draw.rect(self.image, BLACK, (currentX, currentY, self.unitWidth, self.height), 1)
            currentX += (self.unitWidth + self.unitGap)
        self.rect = self.image.get_rect()

    def destroy(self):
        self.game.widget_sprites.remove(self)
        self.game.widgets.remove(self)
        self.kill()

    def events(self, event):
        pass

class CaptureBar(pg.sprite.Sprite):
    def __init__(self, game, shrine, captureColor):
        self.groups = game.widget_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.game = game
        self.shrine = shrine
        self.captureColor =  CAPTUREBAR
        self.percentage = int(shrine.capturePercentage/2)
        self.width = self.percentage * ((TILESIZE - 3)/100)
        self.height = 10
        self.drawBar()

    def update(self):
        self.percentage = int(self.shrine.capturePercentage/2)
        self.width = self.percentage * ((TILESIZE - 4)/100)
        self.drawBar()
        if(self.percentage >= 100):
            self.destroy()
        self.rect.x = self.shrine.x + 2
        self.rect.y = self.shrine.y + TILESIZE + 4

    def drawBar(self):
        self.image = pg.Surface((self.width, self.height))
        self.image.fill(WHITE)
        self.image.set_colorkey(WHITE)
        pg.draw.rect(self.image, self.captureColor, [0, 0, self.width, self.height])
        self.rect = self.image.get_rect()

    def destroy(self):
        self.game.widget_sprites.remove(self)
        self.kill()

class Bullet(pg.sprite.Sprite):
    def __init__(self, game, shooter, entity, color, start, vector, magnitude, damage):
        self.groups = game.projectile_sprites
        pg.sprite.Sprite.__init__(self, self.groups)
        self.currentPos = start
        self.vector = vector
        self.magnitude = magnitude
        self.game = game
        self.shooter = shooter
        self.damage = damage
        self.entity = entity
        self.color = color
        self.image = pg.Surface((50, 50))
        self.image.fill(WHITE)
        self.image.set_colorkey(WHITE)
        pg.draw.circle(self.image, color, (25, 25), 7, 0)
        self.rect = self.image.get_rect()
        self.currentPos = (self.currentPos[0] + (self.vector[0]*self.magnitude), self.currentPos[1] + (self.vector[1]*self.magnitude))
        self.rect.center = self.currentPos

    def update(self):
        self.changePosition()
        for item in self.game.item_sprites:
            if item.isColliding(self.currentPos[0], self.currentPos[1]):
                item.hit(1)
                self.destroy()
        for entity in self.game.entity_sprites:
            if(entity != self.entity):
                if entity.isColliding(self.currentPos[0], self.currentPos[1]):
                    entity.hit(self.damage)
                    self.destroy()
        if(self.currentPos[0] < GRID_START_X or self.currentPos[0] > GRID_END_X or self.currentPos[1] < GRID_START_Y or self.currentPos[1] > GRID_END_Y):
            self.destroy()

    def destroy(self):
        if(self.shooter is not None):
            self.shooter.hasCollided = True
        self.game.projectile_sprites.remove(self)
        self.kill()

    def changePosition(self):
        self.currentPos = (self.currentPos[0] + (self.vector[0]*self.magnitude), self.currentPos[1] + (self.vector[1]*self.magnitude))
        self.rect.center = self.currentPos
