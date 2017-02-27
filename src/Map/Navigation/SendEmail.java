package Map.Navigation;

import sun.misc.BASE64Encoder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

//http://www.codejava.net/java-se/graphics/how-to-capture-screenshot-programmatically-in-java
//use this link AS IS to capture screenshots

public class SendEmail {

    public static String headerImg = "iVBORw0KGgoAAAANSUhEUgAAAV4AAABJCAIAAAD67dnaAAAAAXNSR0IArs4c6QAAAARnQU1BAACx\n" +
            "jwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAu\n" +
            "NBKCAvMAAEgUSURBVHhe7X0FeB1Hlu5MZmYHdufNzs7O23mwZmamOLGTOKY4iVFmZnbMTDEzYxyT\n" +
            "ZGZmZpRtmWQZJFmWLFtggWWR/f7qv+9R3e6+V7I3mZ3k+f/qq3vqr1PQUKdP1W34xZs3b16/fo3Y\n" +
            "AiEtuY48ZCYlJnTZEZYiEHTZO6MnAaVhwEw7KYtg0JkzTBJGvkdGlwkmdV4YJgE7AyDphdFlgkmd\n" +
            "F4ZJwM4ASHphdJlgUmLvMEoomGlbKccsVcBzEUewCGCmbaUcs1QBz0UcwSKAmbaVcsxSBTwXcQSL\n" +
            "AGbaVsoxSxXwXMQRLAKYafdSyjT8IGClUrVqMLPOUUHUdEFkQJcFJDNVs8BSCoLIgC7rsKg5wq5g\n" +
            "KeVJ1mGU+Mk09G6QGv7rVXnH+4beGaz/Fylp6ffC4yJik7yH8JiXFgZBSAiOCgySa1GzJPUg+nqS\n" +
            "wu3HL7gBjni3vWYvRUaPLRDSUcexCKAre4otENJRx7EIoCt7ii0Q0lHHsch7/Izxi+TU9Kj4V2bq\n" +
            "pwBYB1P66SM1NfWVgZSUFJP6ecHRynjHu9mg9w0BP2xDpmlISk5rNOPkl5OO/SOHZrNPYwNoGmR7\n" +
            "vODG9etTJ02ePmXqgnnzFs6fjzB/zlwkEcaNGTtx3Pjl339/984dU/v165cvX1J/zqxZ1JciUydP\n" +
            "+Xb0mGlTpm5Yt/7x48dmAQMJCQlTJk2eMW26FFkwb/6MqdNmTp+OkW8qaYiJidm0cWO/b/p+UbNW\n" +
            "2ZKlixcpWqJI0bIlS1X/9LNO7dvPmjnz7JkzSUlJ0AwKCpo2ZcqsGTOl5vlz56Ezq1asYFWCNX5r\n" +
            "wCNXNGfPmAkm6F6Q414KvHu3TctW6LmZdseRQ4enT53GerAVSxcvNjPcodcMGcCmYQfOman23vy5\n" +
            "c6dNnhL2+LFFzZRc0Jmge/fatmodHx8P2VIqOTl58oSJ2KuoGUcTW4eDZalNT67FDplqHnccDuwK\n" +
            "FMcRh6A2bZ65lxDmzZmjcidOwvHFQVzj53fN3z/VZalZm14zoCctWYCnXC+yJHUe0JOWLMBTrhdZ\n" +
            "kjoP6EmRTdOQkJT6x5brf9HIzwi+ngXvIYtq7xj+vd0m9BvTCsSyAZQJCxMeHr5969bBAwflzp4D\n" +
            "oULZcoMGDJgwbtzYMWO6dOpUsmhxkHlz5hrYrz8v2hjJ+/ftW7VyZaVy5VmkmU+TsaNGz545C2d5\n" +
            "546dihQoCDJ/7jzjv/0WymwFZ+2+vXvXr1tXu0ZN5ObKnqNR/QYY/IcOHkxPS4OC9OrZs2ejRows\n" +
            "WrDQp1Wq4nQ8cvjw7Vu3AgMDA24EHD54COfol7W/yJMjJyoZNngIijx//nzv7j2rV64qV6o0+9Oz\n" +
            "e/etm7ecP3fOaDnjcF6+dGnblq39vvmGaqWKFV/x/XKURQ2iphcZOXwE1HxXrSIjvKHy5l5g4PZt\n" +
            "28aMGsXaEC5euEAFgDqOmDNrNpSxB7CNMCh7du1+8ULN/ryXAqgwZqRqEfufpI60tDTs5DW+fp9V\n" +
            "/YRd6tOrV3p6OrKMTpn1i4AdsnnTpg5t20GzfZu2kA/s379vz96Vy5eXK612JvZz+7ZtRwwbNv7b\n" +
            "cRMnTBg5fHjPbt3r1KxVMG8+5OIEwOWBnRc4NuQJVDBKKJDMIvQimZalglFCgWQWoRexl6VpSHY3\n" +
            "DQjGIG8oSRejCw0hWEhJ6qTIEvRcXw+tUNaTyjRgA+A1cDPUZrlvj4WhHB8Xh/GP492rew8hgdiY\n" +
            "2NYtWoJHWLl8BUnmjhqhRg7OHv2iByEsLOyrL+qwyJLFi0WfCqgEowJZGBKSJcKxo0fLlS5TKH8B\n" +
            "XPNpiYxCCqKDc33Prl0wPX6rfYUHYNFUo9lyPHz4UHjEhMhPnjzhlvbu2VNXQ0xQjo2NLVa4CNSq\n" +
            "f1qNfo3o6EVwWS5euAi3CDtKsnQBEDkuLg6+D/XbtGpFErkEk4CZ1hgASXgcJYoWQ9lqn3wKQ0AF\n" +
            "XY3y3NlzoMOA0cssALmEmTaY69euQe3qlSsmZZCDBw5EJ/PlzvP82TOjhFtDMN/fLVlaungJ6Hxc\n" +
            "6UOUFR2CScBMOzGAzlAGKJMEdBkwVNxyCTPtxAA6QxmgTBLQZcBQccslzLSLcfIa1Jg37UL5ofvg\n" +
            "ydeacLTWhCO1JxxtOP0k5h3Vvj38p9YbMG7/0nZjw+knjNyjdSYdQ9Y/NV3LSlrPP1Os/27KpQbu\n" +
            "RW6t8UfqTITOia+nHK81XhVBqDf1xF/bb4JOkb67vprMho6ioUbTT/y6sdEZ6Y/La/C01qBvmwX5\n" +
            "cufGKULToOP+/fs8yeBdm5SBKRMngRTToAMTkHy5VG3VP6tmUi7ges7a4A6YlAu4XhXIk7dA3nzH\n" +
            "jx0zKQ/AVvg0aHj61ClJIoanwyH3NCKCvCMwumBWoDZi2HCTcsKiBQu5CQiHDx0iad97sF9lSpTE\n" +
            "9R9qsDhXLl82MzwAznmh/PlZc+8ePU3WBr0hXV6yaLH0Cg6Xydrw3ZIlsCC88qNXO7ZtNzNsQOWP\n" +
            "Hj2C2oMHD/SGRo8cJabBpGzAnLGa4Z7AOMK+mKwHeNqiHxx/54ZcpuGVyzS4xiFD/1VXbj+OpXZ0\n" +
            "QnJQeFxKqvLiIGMA5+2149SdSFaUlJK2/WLoPxuV/GeXrVBbe+oRK+m69MK1R9FQC3wSt+dK2Ilb\n" +
            "T9ONIqlp6UdvRhTtpyxI63ln/B8qHSAmIfnQ9fDfNTOtjBka+uoTCqgxFkGSAGQ9qZsGkzKAKT1P\n" +
            "/RZNmyEpuRbTQBKgjIsJcksWLUZSgNMUPE47mgaTff0aBqhYocLImjl9hklpbUGgLEzzJs0w0dCZ\n" +
            "iePHozgCTAMZyRIBiI6OpmkYOWy4SblrAvAFPqxQcf7ceSWLFYcb0rxpU11HB00DZuB5jRHbrk0b\n" +
            "8lQ2KstAQkJChTJlYcKKGPuzl8s02DXtDPDq1Sv2ClMh1asmGb2iggCX9CqVP8KUqlC+/GiocIGC\n" +
            "tFmODQU/CoYODIRJGSRMA8j8uUyvQXgKgls3b8GaQxPXAMwZTdZDQ6akAaTwFplCprBrOpZVVf84\n" +
            "Df0CoxSDLcNrgLMgwUh+MlpdWKA9ZI3/Lxv5VRi6Py0dhV77P4qGwm+brn0e9wrJ6PjkD3zMYTx2\n" +
            "0w0UeZmc+pd2m1iPz4yT0CkzaC8VIoxJwaPIBCYZYGhAouDqEw8U426kEGRCIb1nTEDWSYkBdTnK\n" +
            "ZjUNkCOfPs2bIxeycMVDUnJ100AGkFycmihS98svhWQM08BrO0yD0nOhU/sO0Mf1B3NXMqolrScW\n" +
            "REZGYliaCUPZNA3ZMkyDI0zTkE15DWTsDW1Yt6508ZIYyePGfgvN3Dly3rx5k1nUlJimYeP6Dd06\n" +
            "d4Fmnuw5xbsGVL1azUsXL8GojoiIKFKgUC7s6h5qRgNQR9cUWSfRSukSJTElmTh+AorjoNy4foNZ\n" +
            "UNM1lWn48CMImzZsxAFCxyqWLRcaGkodXRMyjAKOCGKTMjB65EiUygfT8Pw5klIEgsgA5J7de6hd\n" +
            "lD3Hti1bTdalb9G0CBZYikAQGdCTFl5iwlFNh6UIBJEBPWnhJSYoq1ue3EyDLVQavp+qfVdeRvLX\n" +
            "TdZExKjr9p2wF780bMHeq0+QhLnI2X07kr9vvu5xVKLRxOveyy+xklm779x/Gv/rxmuYDHmegCI3\n" +
            "Q2OZZICvkZauuvXd4SCdl+B9QgGwUV0g6KnaJxQzpk0DX/fLrzBUkJRSdq9BaRvA9Zy1rV+7Dkk9\n" +
            "a8d25TUg0DSQDA4Opn6Prt2E1EsRZHReGED3GphLMNdMaBMKeA1k9FwAc/ga1T6fMmky+JDgYFw5\n" +
            "oTygbz9kGTW59UFMw82AAC5hdOrQgVmiA0CGJ1KpfIVZM2bi4g/TAE3LhELXJ3QGvapdveakCRMh\n" +
            "o1fqWp0tR/++fZG0NIQYEwqYBjKTJ05EWwh1atWKj48HY2lITSiy5Xj08KGZNqBMg8trMCmnhgA5\n" +
            "oN27dCEjsDQE2BlHODbkBf+NDbmbBnoKGUFdt0sN3EtVmoYSA/Yon+HNmwGrr1Bt5Ho1GQNXb+px\n" +
            "JJvNPo1JSqpSen3tUcyvDHNw5UHUgv2B1Ee4+0RdP+2mgRONefvu6rwEmoYnmmkwOmJCtWfATLsA\n" +
            "BhMKXD16dutOBudiaEjIt2PGFClQcNiQIXHuC9GA44QCwrNnz76sXRtZgwYMQCWGbkYf7BMKxGt8\n" +
            "/Xh6LV64UDRFEJBBbMkiQ9OAmp8+fUqSuYQk7WsNFs1DBw4WzJc/LCxMVfr6tXIH4JPnLxAeHk6G\n" +
            "ahRSU1NpGiB3aNsemphZWCbe1Fz+/fclihSFzyKmQSYUgFRrgfCHDx3Knydv2OMwJnGYUAP6GREe\n" +
            "TgagMmIxDZBxCLp07ERPrXOHjvKfkcD0GjTTgFIwDSA9rTVIQ0Dg3UDUjFDtk0/JE7qsIys6Aiog\n" +
            "Fk1hmAR0WUdWdARSrWgKwySgywInryHDQBimYZBpGs7cfbbm1CNMH14kpvRbdflXrulDjXFHqDB+\n" +
            "SwD8CEPt4d6r6kijxUrD9v9Hh83JqWlfT1GGgwGmAblPY5MG+l6VMGFLAHs43zQNDhMK5HKt4a0A\n" +
            "04ADjDFQpXJlhAply+XJmatsqdJjR485d/as/ZTSTcOTJ08ePXx08cKFubNnly9TplK58hjt/NvM\n" +
            "ApoGBH0ZEk2Q3LJ5s0m9PcRrgOMDW5YRBmvxkCHf9O6tfGwPy5A4/E0a+XzTq7eZfvPm/Pnz1If3\n" +
            "ZFIaTNOwQZkGf39/Og4YgcwVJCUlfVTpw6mTp0BWpsFYa/CyDGlHs8aNe/fsZSbevLl08SJ7NX2q\n" +
            "Q6+41mAm3ryBs1CnljLWCBPGjTNZF+xrDYC51gDTYPyz6wVRUVGsuWK58o5H/OcNZRrgortMgzEa\n" +
            "tYUGhFID91D19J3IFUfvBz9LwADGnMJnxkmO2L+03ciJwJEbEeWG7MMpWHHY/vrTTrDUymP3G0w7\n" +
            "kZKW/j9aZUxYAp/EIetVSvqVB9EMVx9G3wqNpe3y7jXwz0vA0HU2eBYo05AtR9tWbUJCQuAv3A+6\n" +
            "f/TIEQzaooUK4wLy+aefXbp0yVQ1ANMAnqaBFzEky5cuizGPs99U0sA+OJqGoYMGoywC5qsm5Rms\n" +
            "R48JmAbUgJq7denav28/TwFd9WIarly+nC+nedln5Tjd6331NWouW7IURrihZQIKMqFgsk2rVtCE\n" +
            "Sb116xZ1CN9Vq4sVKsxh5ug1APq2UBaGRsf/6lUmAWTV/6ouKoHtTkxMNFkDyFJeg2YagMehobD1\n" +
            "0Me2r1uzxmQNZZgG9Nk+oQCpew3SGQGZF7GxqBbh0ypVyQv0IpTtldjhqSEv+G9s6Bf40UyDaxyK\n" +
            "aXBNKKDGCcWfWm+4H6HmdfFJKfzf8ZeN/DA1gEJ0fPKGM8HnAp9/4LPmd83WhUWp4/ryVeq6049O\n" +
            "3Y5UFboCvQZPaw2eTMPfOm5GK5xQsPeMCchCCpik1yDLkBLfC7yH8w9ZxQsXCQ4OVgUM6F4DJtIN\n" +
            "69XnKbJ08RLk6pXocJxQjBw+nGXhdXsqSAhvERDraw3MEogOYi5DogNchpR6iO5dusJrgCA8hG1b\n" +
            "zD9c1xqDijwhpoHJy5cu0e6gHjIAbEHVyh+je2YyKUlMA5KsUI/t6NmtW5NGjcyESw2TfGwF6lnj\n" +
            "50deQNMAAZpSJ/pWKF8B6BfMm+/0qVMkAVlrYJJFMrwGwzSQp4IFoaGhXIZs29r674zEjkCW5FLQ\n" +
            "Y09QZd6ylCrwlkUAVSYLpVxeg/x5aQ2+pQYprwGqNA0ICw8EIgmy2tjDZL4/GkQGcat5Z0hO3nbT\n" +
            "1dib0Ruuk6Rj8k6mwTd7t23IxYTCqNMEGMBMOAG59BpgGoSRGA6qOvzZckyaMMHIVNBNA5JPwsIq\n" +
            "lCkLnXy58pw6eVJvTpd1r4EMchcvXMT6eYOjHdCJeh61YN78JYsW6eHAfnP1FzBNg+sfCvAEcwHK\n" +
            "8uel/EMhePDgQb5cuTEzt7Qyb87cvDnVzqlVvYbl9ifTNBgTCjItmjZD5ajnZoD6UwP8+rVrixQo\n" +
            "+PTpUyropoGlEBNUAESG8PDBg/x58mKSYunV3Nlz8udWi5E1qn3ONR0pKKZBQH7rli1qypMtR+ni\n" +
            "JR7cv09erTVopoHgMqT9HwpAbwg4cfw4LBRqmD93nvCICUNFQWSdJMjoMWFndEguQRIQWScJMnpM\n" +
            "2BkdkkuQBCibaw3Jqen/1la5APbAtQaoimlYdfyBqunNG96SgNB58XkyETEvf998Hcl8vXZwWRGo\n" +
            "MuqgQZrLB4+eefuHYumhINHUgmkadK/BIthJwvIPhZ57zd8fWQhdO3Umg1z7MuTpU6dxMoEsV6o0\n" +
            "piTU1AEdy4QCDHD+3DmSn3xcRVYuLYiLi1u3du3C+Qv69u6TJ3tOnI61q9fQbzHK8BqMQWiyLrAh\n" +
            "CLppYJZg5LDhFcuW6/9NX3to1rgJKz929KipbUCfUBDnzp7NnV05DtyTUPis6ieYlzEXeNu1BoxS\n" +
            "2FxLfxiaN3H16ohbryxrDQLsAf7fhIAZInYFSA9rDcY/FF5veSImjZ8ATZw8MGEm9f8TlGl4Ev0y\n" +
            "JS39L203cgS6D0i/CkP3QQe7nqYhb88d0QnqDpB9V5/IjQzFB+zhGTt+SwAZhuM31an84mXKP7eA\n" +
            "vcioGS2Cf/A0XhiEXD228xRfecy4ryEjmAUL9N6JXMe1Bj2py4xlQmFkmrkA5Nu3b9N3xVydDGK7\n" +
            "aQAwdEFC+asv6mCWQRJQFRlw9Bowfj6qWIn8iWPHwUgWY4IyhhamNtAcNXyE6AD6hEKUJaYAWEyD\n" +
            "ZEVGRhYtWOj7774jaYmjoqJ4R1brFi1VAVdt+oQCScTp6enNfBpjD2C0BN69u3njJvXvxhP11zWh\n" +
            "rzVIPYQ9GR0VBY+DvdJBzZiYGN6N1qpFC523TCgAkdG97l278mi2bNYM/TdNg9Ofl8pr8GAaWGFi\n" +
            "YmJF41GaHt266bzAe1KHpyyd9yQD3pM6PGV5qtx7zYZpUIPtDWby2mg0w6981szefQc6KBb4JO5I\n" +
            "QETcS3U3zp4rYf/eLsPL+G3TtbAXsC+5e6hbG4ygxnP7hWdRFqWEKd5/9/dHgnjTVHJq2rIjQXl7\n" +
            "7QD/1eRjuy6b4zAsKnHxwUC551pMQ+G+u9hbqiEmIBOWJAFGX2sQMFf+u4bnTAawmwbEuOZ37tiR\n" +
            "yv379sWJSOWMqmxeA2Jg9apV5L+sVRsuN5V1BYByhmkYMVLnLWsNzAIs9YhpsNwNOWvGzBJFiupP\n" +
            "OjEWDB4wEKWwvXwOFQxi3WsQ8tSJk1CDco+uXeHtWxY1vHgNVCOoPHf2bPQqNiaGWXpM8Lk4TBNu\n" +
            "GwufzLIvQ+pISEioW+dLlEIYNmQojAIETiiQyzjjbkjtHwq9XcjARMNlKFm0mKOTCNiLEExaYoGe\n" +
            "FAWdtEPPpTLBpCUW6ElR0Ek79FzI5loDJPjzrqFoBGMNEuPzmxWXuy690P27C/1XXUHovuxi2cH7\n" +
            "xF8wghq6vZdf+mbFJRnGDP/aesPkbTfran9bwulAJd2+u9BliQr9Vl7O1lW1+9HIA5AN8nyf5Zeg\n" +
            "85sm5v1RruDLvz/EayC4GTqMTXMjHb0GCJhdN27YCBNUnG1wBKSUZa2BQG5sbOynH1eBPnKXL1sm\n" +
            "+sT2rdvAI+heA4BW2rRsxayB/QdwSq9D6tFNAxlmWdYahBeoZjx4DRgt5UqXGTZErXQISUjy+vXr\n" +
            "KIXrLWwEGcA0DZrFJHzqN8D0G/oF8uZ7HBpqskZtaq3BdaO0yRqwtAvgmlyhbLmhgwZLlr1vN65f\n" +
            "pxka2K+/ydrWGuyl4MW4HpzNCedL1hpE05xQGF6DzgOUYfQxbcmTIxc28NCBg3qWwJLUIVleirxb\n" +
            "lgWS9W61eckiDNMQrUZFvt473YdiZoH/YrgFN7vgOWRRzRqqjFJP3cA0cDMQWwSRCSZxiqu1Btcy\n" +
            "pPA4OzFgMB4wGi9dNP+8ZNa4MWONcytHUJD5vgOJA27cKGr4zPlz58XcGwyBXN/Vq8GjQq4gkqSA\n" +
            "K3bLZs1ZZ+cOHSNcNzWKAoWkpCT49qiBpoFZwPAhQ1lWXhVB3iJg4pAvpzKCvJWQPH2WC+fNxSBq\n" +
            "2uP6X6v/CzFBCAkORhKAncI1ExsFmToUjh45ws5wCqYDu7RQfvVPQcd26r5JKaWDpK/RK30HEsiV\n" +
            "UhD431DBvPmkV4sWLIBNoSyQIoT/1auc19CIW9Yahg0eAtJxQoF6bt28iQMEBWz7oQMHzAwXLA0R\n" +
            "JB2zAPCesrzAsQhJT7WpZn7Qhsy1BsSlBu7R/7O0jnw96ajgPdgeiHAPeq5HzdoT1IqUbhosG4ak\n" +
            "gAyuAPv27FVnSfYcn1X9ZOH8BX6rfVcsX47rasWy5XBRaliv3p3bt6kPIxIaGnr1ytXa1Wtwyjpl\n" +
            "0uR7gYHPtOdwgE0bN3I1rnzpMriqYEKLgEtc21at2dA3vXvfvnUbly+9VHJy8sL58+FCo2aMf/j8\n" +
            "J0+exDwfPJCYkHAzIEA915A9J3o1acIEFgkJCbly+XK1Tz5lzbNnzkJ/nj+z3quDeu4F3lu0YCEH\n" +
            "Q5UPK8PzRx8OHjhQungJMBjhQffu0TMipG/h4eE3A25ics4mmjdpCvOHfbJh/Xr0pFWLltg0fVuw\n" +
            "SxvUrQc/HzxJxOgqzOjqlRjwOXGhhp+CYY8xabldAgh/Eo5elSpm9GrV6nvolfvNCwAqjAiPCAgI\n" +
            "aNW8hdmrps0w2fH394eXB0O/c/uO+/fvW+560LFrx05c9tETeg0kUe01f//ypcuiQmwanJE5s2Yt\n" +
            "XbIEYc6s2XBhcNzz5shVpEDBAf36wyFiqaxDdpEdsqOYBOxM1vH3acicUCB2/YngKWTlUg8dpfar\n" +
            "xmv+1HrDn9ts/HObDX9suf4DH31qYK/HsWadNGWfGeqPQ/Y2i7h+7RpOry4dO3XppEK71m1w9W7R\n" +
            "tFn3rt1mzZx55fIVnOimqvEKEyjjokFlhvZt2o4cPsLUcGHOzFnQ1EPH9u31Uh3atQcDW2MWMIDD\n" +
            "ExMTg8t4m5Yt4aurkz5HzmKFi+ACVbRQ4ZrVPsd1eI2fH8YYnFros/OdO1r7A5eYtRm1KmBijE2T\n" +
            "zYTQukVLlO3Qti2ZTh06ICmPhEtZbH7Hdu0x3zELQrO90jTKtiPTrk1bkPq2YGzLI5tEwI0AsxL0\n" +
            "gd0w+mD51wNFenTt1h69MtTYK9RmZruQlpbWo1t3VaEcuDZtoIkKWTP2CZI7d+wwCzhh8cKFlcpX\n" +
            "gIG4e+cumYT4eNSJqsxqO3bCtqOepj6NEeOUmDRx4t49e17EqkeNsw59P/yo+Ds3lGEavpx0TDkC\n" +
            "EjAalcBh6YozeJcODEEjv8J9drZfcHbe3ruHroc/eBofn5T6KiUNISk5LS4xJTzq5aXA52uOPRy6\n" +
            "+mrtb4/8rf2mX0rlUo8eewidF59Hpy0TiqyAyj92ESCL+lDDSIMzEnj3LjzYkOAQ/uVhZmcBWW/I\n" +
            "lFzyW7UCOOqDtFi9f0zAxDx//lz3ld4j68hYhmztulXJKThe2NV9kH9osjZ7203R8eq5bICVUkCc\n" +
            "nv46OSU98WVqzIvk8MjE+8EvbtyJOnc1YtmOuy2nnvy/HbZYKvQeBvleRZ261yAtArosAElIkoJ3\n" +
            "sIiADLMIPWmoOMDM1qCT1LGAPBUIPWmoOMDM1qCT1LGAPBUIPWmoOEByJdah8/bcLEIv6Kkqnbfn\n" +
            "ZhF6QU9V6bw9N4vQC3qqSuftuVmEXtBTVTpvz7UgY62h74rL5jWcAaNRj/XQUBmF3zZe89cW6/O0\n" +
            "3dRm8nFpRlolHE3Def+II2ce7zj0cPXWO33nnCvYbbvLibA15B6m7riFOnlPBCANGS1bN5VJI98Z\n" +
            "oqNDeIGetMhMiiDwlDQUFXRSoDMWmUkRBJ6ShqKCTgp0xiIzKYJAT9plS0xYGAh2hrGdYRIwMhUo\n" +
            "W2LCwkCwM4ztDJOAkalA2RITFgaCnWFsZ5gEjEwFypaYsDAQ7AxjO8MkYGQqULbEhIWBoDMZpmHS\n" +
            "1puevANL+I3Pmj83W5ej9cZiHbd82G37dztuozjAGnVgKuvFNPhtu7tozY3JS660Hnv0b214w5W3\n" +
            "sPKYuintrdYa3g32DckU9iKZVgKFTHXssBfJtBKjnR+4IZEtpJ4UWHRMySWrMgZENjJNSFLnDS03\n" +
            "NcKiY0ouWZUxILKRaUKSOm9ouakRFh1TcsmqjAGRjUwTktR5Q8tNjbDomJJLVmUMiGxkmpCkzhta\n" +
            "bmqERQexYRowoXjzZvnR+8YIhHUwgrqGu5YYXMKvGvn9sena/9tqY6H2myt02Vat566v++45c009\n" +
            "V2+0aNZOGfDuNfhth2kIgGkYOuts13HHS3Xb/gGtQMbfGdIBFe/3Vzfe0ZDprYhsFwClaiR1UiC5\n" +
            "lEUgmLTAkiUyeYBJCyxZlEkCJC2wZIlMHmDSAksWZZIASQssWSKTB5gUmKx7EUnaZcDCE8KIQEiS\n" +
            "AkAe0JN2GbDwhDAiEJKkAJAH9KRdBiw8IYwIhCQpAOQBPWmXAQtPCCMCIUkKAHlAT9plwMIr0xD5\n" +
            "Iik1Lf3g9XDXgHQPhp8Pn//3Tdb+reWG/O02l+m87ZMeO+t8s6fRwP2thh8KNR7ElHoBVg1k0TQM\n" +
            "m3W2+/jjzYYcrNB1xz+53gRlDwEhsUkpadHxbi/qA8yEZ+g6WSxigaWUpxosOp7UvMBSylMNFh1P\n" +
            "al5gKeWpBouOJzUvcCxiJ3XmHVoB3jcE/IANKdOQlv76aWzSnbAX2jh0u25juP578/W5224q2Wnr\n" +
            "R9131Oq9u8GAfS2GHew4+mivCScw8lGJpXYkgSyZhqUZpqF6790F22/+tflnp/RBhV81XhOTkByd\n" +
            "kJyUnPEo3luBXTIT74p3a9eU3gb/yA15gr2qH2kr3jfkiB+2IWUagLDoly+T0/iyNhW0FUHMILK3\n" +
            "3li0w5ZKXbdX77Wrbr+9TYccaDfqSPdxx/tNOTV63gW+YxoNSBuUAQfTcNdqGqa4m4aiHbf8qZn5\n" +
            "7KYe/lfHLXBtImJeok42IQ0ZbSpIUmJCZyQmVLYtCUjSQkpMQaAzFHSGsCeFoUAGsDBKwwWdoaAz\n" +
            "hD0pDAUygIVRGi7oDAUyAEnCkhTYNXXYs8gYhcwsEQhLUmCUcM4C7FlkjEJmlgiEJSkwSjhnAfYs\n" +
            "MkYhM0sEwpIUGCWcswB7FhmjkJklAmFJCowSzlmAaRo4gc/ebZs5FE3T4It5BGYQ5bts+6znzq/6\n" +
            "7mk86ECbEYe7fnvsm0knB884M3ruhZnL/TFiUdbeBpi3NQ01eu8u1nHrv1pNg3IfKg7bjwplocE7\n" +
            "2JmHDx4+uP/gcWjoYwPBjx4F3g20Pxl9/do1eeUhChKQg4KCgoODWfYR4P4AH/D06dMd27dPmzJl\n" +
            "yMBBA/v1Hzl8xOIFC0+eOGH/Iz0xITHo3j1WFRoSci8wMB27xtUQIIId6AMaV1sRGorNiYqKgnLY\n" +
            "48eBd++GBIewTgEUwGMzJTwxbmTEVoM368GucN137Ano5IP7981dFxp6P+i+vLsRtcVER2/bunXd\n" +
            "2rVkiPj4+D2790yeOHFg//5DBg2eM2v2sSNHecOil62LCA9H5aoVwmjxXuA9NMFSjFNTU7EtIa7D\n" +
            "QWBz7geZLwohRH727NmuHTunT502eOCgATg0w4YvnD//xPHjicb7gS3QG8Jx8b96ddGCBUMHD8Yx\n" +
            "nfDtuI0bNjwOzbg/PS4uDj0JuhekuspgALuLOxwyeqvqdSEUB+7BA6UkRUJDAwMD+apb45Cpgtjn\n" +
            "SodqAkM5JCSEhxXKrNOC27duWW7blxh48eIFmjMrdAEd5tODhCgL3ExDjXFHzNHoWgj8Q5O1Vbvv\n" +
            "+KLPbi4rdB5ztPfEEwOnnRk5+/yEhZemL7u6ZN3NNMM02IHG/kumwf2/zLYL1JbDu2HlFtg3DBg+\n" +
            "dFjVjz7mnbYIdWrW6tKxk35ysJRPg4ZttE/USFV9+3zTqH6DQnnV9w6qVf101AjznkgoXL1ypUPb\n" +
            "dvlz56ldvebggQOnTZk6acLEHl278THeEkWKjho+gg9QE7du3erYrv0XNWtJZ/h0pgX2rQgLCyts\n" +
            "PJWAUOXDym1btT54QD1IsmDefPT5w/IVeGd0ruw5CuTOW6JoMYTihYvwISuGul9+Bf2E+PiunTo3\n" +
            "rFefWXlz5uYntgSWpmdMm9ayWXP1VYjsOcqUKNmyeQu0yCzYPn6gYdGChWRSUlKQC+Ua1T4fM2rU\n" +
            "1MlTBvTtV6XyR9ApXrio/o0pO1avXNW2dRscJmwCb06vXaNm+zZtLbdR4uRG/xs3bFTA+PAcQrHC\n" +
            "RVo0bTagn/U5joAbNzp36Ige1qz2+aD+A2C4J0+Y2LNb98oVKhr9KTJsyFB9SOg4f+78l7W/KFGk\n" +
            "WJ+evXBMR48YiRaxx/LmzIUzITYmBjpXLqtDX/2zauwGQv5ceSqUKftxpQ9x9PlmkLKlSo8YOkye\n" +
            "lJk1c2brFi15biDkyZETB6VT+w7X/K9ht3/+qXkXfFYC9jDr1IFKmjVpWrFs+SQPN3ddvHABp99X\n" +
            "X9TJY9zgj4C9gXNp4YIFpoYTMiYUiOVlLWIa/tZqQ/3+e5sPPdhh9JGe44/3n3p6+Kxz3y64OPW7\n" +
            "K3NWXlu8NmDV1jsyoTBqygAYu2kIeAuvQdYalDBth3oy15Np8ISEhIQPjXMCx9vxBj71+qOcuQvm\n" +
            "zYfrjEm5o3/ffuXLlBVHABeEaZOn4Ayo99XXuLxYthpN7N61S52FxuuGLF9hwhVJvePUGMwojqSZ\n" +
            "4YJ9H37Lt85mU+cfrlcm68Kjh+rrTAjdu3SNj4t/ZSApKQmXIzgUB/YfqPfl1/379tWrHTZkCGrL\n" +
            "lS1HkYKFxFfyhIMHDqDyM6dPm2kDSxcv5tbFGEMFm4yzHPtwxfLlukcGfvXKleBHDBtmUp6BTaPN\n" +
            "+qzqJ3a3TseUSeqhWIRNxuunANk67M/ZM2flz533y1q1L164iJOPPIEDt3/fPhos2O49u9QD/jp2\n" +
            "7tgBg9KqeXP9TEDlt27e/PyTTwsXKKifP+gkTA/2Q5ECBaOjo9lndADmYOb06TQQsAXw96gP3L59\n" +
            "W43MbOZXAgUN6tbDRsF1wmWcYe6s2VBDDQvmzSODXIQJ48bhMmYW04ALD99wtWnjRpNyArYFbaFa\n" +
            "KPNxNfv5psPNa1h13PIOFb98Hbe0G6mWFfpOPjV05tkx8y5MXnJl1oprC/0Clm285bvt7sY9QS9f\n" +
            "mU8ZowZpjIxn0xBmmIZAr16DmAYVDl0PR9UyoWBDbMUCKgCU+TICHC37CwsQ46qCXITlyzLe3UgB\n" +
            "wMH+9OMquDRJEk4mlNu0bGmfNVAHAvwFWHeo4VQ7fDDjy3GA+lyN0RzOEj7RQF7XASg/i4yEy0BL\n" +
            "jwsReVGDABPAz7fysWU9l5g0YcLUSZN1HmcbeoULF0rB6XhivHhecqkpgJsKNTm/qYBLOsili5eQ\n" +
            "gb+ApOXFDYAqoN4W3WSx9pZ9ga4DQOZLOps2biJZjAmSgJ+vL9QQzp45Q4YKODT8xm8THx/9sStd\n" +
            "B3j+/HmdWsp3y5c7D4w4sxAHPwrGIC9XujTfH0GegLx18xZ5cyyzEHdsr17AX6xQYeGZBajn3IxO\n" +
            "9uphvo4UgAWhydAfRQfgnfE7hmZae4fI7p1mD4njx47DrFDHpIxScI6oD7cU+4EkcwFDXQEyHFuo\n" +
            "FciTLzk5mQwVJNZhmoYXiSkJSanaG9mMYdnQr1T3HX0mnRw8/cyoOecnLro843v/+b43vlt/E87C\n" +
            "+l33th54sOdoMIY9akhKSTt/7/mSg/cGrr7SYs7pOhPVpysRmsw81fv7S9O339p1PjQgKDpLpsH1\n" +
            "CjkJv26yJvJF0quUtGdxGS90Vpvrvj2OSeNBSbXjMNUUksAO+rC88ikQvvriC5K6Auw1TOye3btJ\n" +
            "Gk8Wqsd1M313GKZ2/PJamZKl9KsQvGJYbjrkuALwQOqQ1iFMHD+hUrnyfFy6XOkyescI3TSYlAtU\n" +
            "njVj5jL31yjNnzvv808/43tKEL7+oo6X5QDMb6HzRHuPE7YF2wWfH+4JkriQ8m3Oly85fxFz0IAB\n" +
            "69a4LUl4QiVMjrLngCkx0x4g3/WQx8wJfsyqWKEiniYLgkcPH/KtVvAd5BVV/NoNriJMWgAfhPMy\n" +
            "HZ06KCsvpkFHbGwsp4FVP8p4IZWYBkw2yRDtWre55u9vJgzAf4EagnyRlMBOxozYTLgQER6OIyJP\n" +
            "/crXUh2BiR50oG+mvcI0DenGswmvUtP5kmgJFXrvGjH73PiFl6Ytuzpv9fUl626u2Hx77c7ALfvu\n" +
            "7zry6MDJ0GPnwjYcfwgT8C/Ob511C79rtrbi4H3ztt8+cznirUwDX/2G2YTjGSxwzMVFjxfJWTNm\n" +
            "mJQL+/buxdDq10d9e169Rv2mes+tjpXLV0CB7kZMdLR6lFi9YFZ9ZylTyLukp042r9sATEPfPn3E\n" +
            "zPOVcI7AIMRpt3rVqtYt1IPJ5Up5NQ39lWnwvnMIeA0wDXCA+VYChG6du3jy4Wkawp9kfCpmxrTp\n" +
            "OLE4E0Fz8HVZCQQPOz840visjsBTJ03T4ONgGvQijqZBva7GcDpGjxxlUl73xoRx4+mxjx09mox6\n" +
            "o4/6ksV4JgmpAR5iwA31rUYdYhocG6psvPXvsypVJdcwDWrSJG/3IzCZsixb7tqxE2oIFtMANa5c\n" +
            "6oBRw1UNZwvfl9ehbVszw4ClbzQNBTMzDSxlmgYgLEoNPAxyNRrpzDf0q9xvD4bu7JXXMIaXb7rt\n" +
            "t/3upr1BGNX7j4ccORt26mL4ef+nk9bfkDGsBdTAGQGrkqTf+C0BsEQRzxIPngp1NA1/VqbBVGZP\n" +
            "Oi46h76FGd/LY28hULbHAiHVcp16X2BzyCSJVs1bwMW6d++esh3Z1DdOqEBAAc5zi6bNqLx82TIe\n" +
            "sMuXLjFXhxShjNjf31+df9nU8qF4B8o09O5zPyiIU+umPo2llIDMtClT4dHg4oxOQlO8BsQUAItp\n" +
            "0AEdfuqKMgGZpgECzsg6rmVRGC/m6gAjpoG5jx8/xgiEFyDKN4zXQyFs3qg+EQJeskSmAJAHRKbA\n" +
            "WLwGpaoVVHoGKNtNA3h0iSTfNE8eEJmCJO/cucOrRfkyZeH4gP+iRk0cKXU4jH+OqAZAlqTIFDg3\n" +
            "pNdAXgR4o1zBxVXH0FWwew0gKQDUoSxew6GDGe+YIkSmAPcEDW3epHY+3/cD63MvUL3wXTQBkS2m\n" +
            "QdQoAOQByBmm4bHx2YhpO26ZY9II5b7Zs8DvxrINallhw+6g7Qcf7j0WfPj04xMXnpy7GnH5xrMb\n" +
            "d6JOXA3/wPMtjPZw9q7pYKP50CfxyzffdjINyjBJkTUn1R+HMF4sZZRW0GUv6NOzF/YIJgIYaVIE\n" +
            "kwUcqjOnT4OBbw+FSuUq4KAyF8C1Agde3raqXrWeTb1x6EWsciIcoffHOD/UC0sQ6Lgit4vhNUCG\n" +
            "gQCfJ3tOyyIfa8CMt3jhInzHnKPXQNmTaUBuVFSU42o2JhTVDdMAndCQkApljI+7ZM/puIJlmVDg\n" +
            "5La4688iI3m6lylRCpc4+/wI0LvtCCrQNHCtgbwjNNNwwaTevOnYzvjuXo5c2GqTcoLUDIFeBjYc\n" +
            "ZhqM+FDjx34bb1vutYD1eJlQHDl8GFmYNl6/dh1J6ovXYH/9hwXiNViWse1YsmhxxbLlOLnDweKx\n" +
            "GDHU47qvJ6/BcZ9nmAZMKNLSX18PjlHPQTI09CvQffvKLXfW7bq3df+D3UeDD556fPz8kzNXIi5d\n" +
            "j7x26/mte9H3HsY+Co0rOcD1hihPwZX7fzpvSXb/RyMt7fXR8497TzrZbKiYBmNu4vJcft1kzRPj\n" +
            "TqcnXicUyLLnkvRdpV7NhqC7hfARqn9Wjb70+nXrqHD0iPmRPpQ6dfIUri33g+4jiZO+bMlSUChd\n" +
            "vASvM1TTYScxQWW1ly5eJCOmAaadB5LXSeYKZs+cCU8nMVF5Sa1oGrS1BhG8mAZcz2vXqGmmDbAU\n" +
            "vAaaBgId45pIoXwFzp87Z7Iu2CcUhN4TrmwhYF9V++TTIYMGL1m8ePeuXbgyW/xk76Bp+KJmrXNn\n" +
            "z3oJ48aa7+bTJxT8Ewr7UzqWKZo08mE9XIHCcVeL/AZTokhRWIpZ02dsWL/+/LnzmE46VuvJNDx8\n" +
            "+PCjSh+itlUrVpiUAU9rDYTeRBZNA85DbPi8OXOZRA3tWqsVB8ws0JZjn7M4oSAyTAMcKWMyr934\n" +
            "1Mjv31pv3LT3/q7Djw6cCD16Luz0pfCL157633x+MzA68EHsw9C4x+EJT5+9HLfRcU7hEHp9bw4S\n" +
            "HdiMR2FxvaecdDMNrlBhqLrZKSJWvRHfLJA1yN65e0ed4ggrly8ng1FXtlRpXpYBeNd8X2vPbt2l\n" +
            "1MRx4+F780qIsxynfi6XaaBCpmjXug3blcUhMQ1Ar+49kIVqLStqykssXgIXBCbtpkGAOTZNA85m\n" +
            "uMSwRAzVP/uscP4CDerWM/U0iNdAoM6tm7fkMUYFbJ/+ZxsQGKi+B6svQxJ6T2CevunVmye9JWBf\n" +
            "DR08OMT1RmZ7/wEhaRrQk2KFixQvUtQMhV2CK3AFF0G8BnhnGJ9gxDR4b4jo1kV9ChiBww+5mzZs\n" +
            "pPW3BEz9YEcO7FcnIcsSumk4fuwYLj/r1q4dMWw4mJqffy7XGIEyDcYsMlPTYF+GdNyiLZs2oS39\n" +
            "bYAwcyyIC4BjkUyXIfVSGaYBCH2u5hR91Jfvzan+Bz5rfHcHHTkTdvJi+PmrT68EPLtxN+rO/Zj7\n" +
            "wS9CniSER758Fp0U8+LV/SdxxpcmMsazUYNrvcDFwB+BV8K2AL0fkGPiXvWcfsrdNKga+BUsfpif\n" +
            "ygLH7bdUixhnD4YWdgrfNw9yy+bNRQzjamgphv9KFilQMMb1+vNa1WuMG/stFWAa1Psgs6ndCoeT\n" +
            "CogpULbHytE1lrv4GkVAmYbefSBAQb7H36p5CzIshdELdxfDnknkwiTJhEKPxWuo//XX69euQ9ix\n" +
            "fTtiWMBunbvU/7quKFMA9AkFGQjyD27Nz6vDMAkvEwoqI6ZgB3yr75YsgWFVtwwVLcaZPAMMBOox\n" +
            "9TRYqsJsDsqNGzbCPC7JM1Z8v5zVij0V04ALNe9KssCxzz27q0+Z4ujovhL2J4YlHPLGjXwqV6go\n" +
            "9g77H1s0f+5ctf2u2vS1BsxKsGOxG5cv+/7qlSt2dwmlPHkNUqFAX2swKXegCLzdOrVqDxs8xKQM\n" +
            "4DKGI4iC6DxnGTpQyvQa8rqZBnsHCDfTYAw/9UVs18hUYayv/7krEZdvRF6/HXU7KCbo0YvgsPgn\n" +
            "TxMjo5KiY1+9iE9OeJma9Cqtw8JzeinHUHN8hillhxDrPUtMSmk37aRhGkzL8qvGa4KMJzstpkGX\n" +
            "CUeGgKwmk9lyfFSxEr2AhvXqD+o/wNAyC168cIHDeI2vL5jHoaE4kKdPmQsBKIXpNBVworOIBUKK\n" +
            "oJYwsqnLjtxSwWVI5gJIokLdcYBFKFO85IJ581kJ4tbNW6ASfa1BBC8TitDQ0C9q1DLTWhGL1wAg\n" +
            "C1unPqhvLJq2adlKzmyLacgioBwSHPz9smWVyhr3/2XLYf/LzQ6ahsz/vPRz+IeCHgdCpjdxCVq3\n" +
            "aImO4RDLYq0dcS9e4LotrwIvkCefrqybhqzAMA1ZWmtQpsE40yz/UOg4dvQoFDDfea4hKipq8ULz\n" +
            "lgo4g6aqhnecUABJKWmRL16lpr8uoL14vt7EYxnLCo/jwiISnz5/GRXzKjYuOT4x5eWr1FfJaamp\n" +
            "6UHhcb9zeixKAgb56TuRZksGHE+4uJcpJfua38tDqDxCvVM0GgbolcPENeun7NLFS7jLwh4/DggI\n" +
            "yJsz91Xt68wAzDCmD1Dwqd8A1W5Yt57LlswF09SnMWtY4+tH0jsw3vgPVqN69U3K9Q+FmXjz5qbq\n" +
            "iXLmMfVAG2Awj4BbS1NCmMuQXicU9n8oYDWaN2lqJjTYvQYC+l/XqYOqEEaPHMVcMQ3UeVuEh4eX\n" +
            "N5w1WGST8gA0J6bBvpk6HE2D/KtvuYnDDqn8sypVoV+nZi3vzQE4jgP6qRGFoP/Z7N006NVS9uQ1\n" +
            "2KHWGjIzDTBYuKJgkFtCgbz56LLVrfMlr4I6zAmFu9fgCW6mAQh5rvzYSdtuyuD8j/abbwVFPwiJ\n" +
            "Cw1PiHj28rmaQSTHJ6S8TFJGISUlPS0NfcDmvx7kd1VK2UOTmep90BbYD0yy+70Vy4+qVUDMdETT\n" +
            "XkRg1xHB/+pV7BSE7du2DR8ytG6dr5iFWISF89WNfRircI97dO3WvWtXo6gJscctm6s/QU3WMzDH\n" +
            "5rDfsG6dSbnuazATRqNcXc+TI9eVy5cTExMrlCmrP3QAhXczDZ5g9xoIVA4TwI+74NxauVwtoXla\n" +
            "axBkuh8mTVC3En1U6UMz7bmI/uelSWkQ0tE0oLckfeo3FE0vfVMf7DBGKT9frsOxFM4HjreTJzLO\n" +
            "Ye/3NeiggsVr8FLKvtZASJEbN26gP4cPHsIJA2AKBog8Y9p0Fj9nPHClN5RFr4FFrKYBfjtG+tPY\n" +
            "JPmqLcLaYw/DIxOfRasZRFxCSqIxg0hOSU91GQUCF/w8PdVX6lwhY63hz202hjxLEGWzMacdtPdq\n" +
            "mJT6n+03Jai3zLzmPRemhgF7QcCLTkpKCo5irmzqIxElihTFhNxeQ1hYWP486gPNkydMLF2i5KaN\n" +
            "G3WdpxER6q6SbGoY22+AsdfGu2UxDl8lZcz6dK+BRWCzxHHATLVk0eKc7TMXsXlfg/taA5ExobDd\n" +
            "DSm4fOmy3PMHwDTwvgaBXi3myUUKFMQ2YhJ0/NgxT16D3odlS5eiG5R1nvL336mbQdq2bk3SC0zT\n" +
            "4ONmGnSZcLyvISYmBscUZJ6cueTPIC9QpiRbjo8/rIyxRAa1nTtr/YMGYAdioqML5MkLT/NJmPn4\n" +
            "KaC8hmxZmlCwEtNryOY8odC3VCYUntYa+vTsVfPz6o4rGohxuNVibbYcnW3zOItp0BslwCxeuCjJ\n" +
            "+HqI1TQAGMOIOy0+L0O03uTj2rJCanKKmkGIVWAp4sStyN80kW9VmuGXPmtWHX2gTIkqxULWPgma\n" +
            "zjolBUesV1NHw1p51M86OMYwsEsVK85FPjs6tG0HnUL5C2Bs2Geh8+bM4THDHEG/A8IOnKzly5TF\n" +
            "MdA/eI2t5jKkmXaB/jCuAzi/Z0ydRpK7CLFlGVKHJ9OgazasV1+/D9dxrUFiYOf27TRVJYsV27VT\n" +
            "/YvmfUJR9aOPMS0yEzb0/6Yvati2dStke/91iGkw0x7g6DUA3y1dSv7L2l/wzPaEuLg4eDEYPPod\n" +
            "JYsWLhzt+s6oHZjSo2ZM0PRN8HJfgyPebq3B2BYxDXq7IcHBOOirVqw0007gP8r5cue5f1853YL+\n" +
            "nFB49hpgVnD9Y3MOpiHYMA13wl4YH6RVV/7fNlt7KySWMwgM77Q03jPmcKRBTt0uN02ZXkOnRedh\n" +
            "VuBrcAKi+xpmMRfCY5L+YHorvn9stT4iJgkaMFV2TTsy1Zk723ygbYzrdlp7kT27d/OoNKhbF0mL\n" +
            "Auw0bQdCvz7f6GZb13z16lWndu0x69tte7wP0wd9QkFcuniJn8PCefZc+0Yr0ayxWuOQx6v0hjJM\n" +
            "g4cJBSwgzI3+EMf8uXOrffKpmfCA2TNmcht5KaZpsO8rAqZhvTZjEkAfvhXOwqY+Pp5uxNZRxvjj\n" +
            "0OI12OG3OuPxKjLURxNyh0XPbt3t6/MEDhnUcPXeunmz3tCC+fN9GmRMRnTA32zm07hw/gIWC8i1\n" +
            "hgJ58plpz2C1stYwZNAg8p6wc7tpGvbt3WtSGr4dMxYHnY+9WiD9x1SCNWDuTIbo00vd+yf/UNi3\n" +
            "F2fspx9XoexgGlJS08OMOyPbLTjrGuR+3b+7qC8rUFOHkPjtsuSCFPx8zOHHEYnPo5Ni9RUKZV8c\n" +
            "DMTI9del4GA/tUwYGZuU6LQA6Qi9KjvgMWK/4JJ4y/jyMmEpggtOKeNLcPPnzTMpd7xKShrcf4Ca\n" +
            "eWbL0bBevYsXLmIzzDyjNlyiG9Sth+uS3UFNTUmtUe3zRvUb6EUAlKJHw8/Y6oBvUuVD9Rwxzqpo\n" +
            "261+gXfVWgACLA4GMBRw/iGGLXgcGnr92nWcRhic+sgc0K8fDJAnp4lA93r37MmaEey3PAkw0sqX\n" +
            "LlOsUJHVK1claLf3Yzht37qtTImSsGvyb6gXwCBy2FSt/JH320bklifYCJNyAQVHDB1Gl+frOnXO\n" +
            "nztnMUkwVU19Gn9YvgJvc9KBKSRK9ere44H7ZfbunbvqvRXFS/AxWUFaalrt6jXYk9CsfQLvmv81\n" +
            "PkSLfeL9RJ07e46qOVsO+/sU7ty+XTh/QcwmLKeQBbCM/DwajEjADdOivXz5slrVT0DCY4I3IWcL\n" +
            "gEN8586dI4cP1/+6LraX+r9w7OXDyHiwcB9cdyv4/qHF+gcR8VC265PR+ZS09GazT6NgxSH7rt2N\n" +
            "svyvkWD8r5FsOiAZBiI6Pvnf+Mr5hr5/bb8pKl4ZfrowVDDqzoCdscCigGts4QIF4XAK71in8bRV\n" +
            "zhvX1WqCYxMgcaI0bthIGYgcOT/5uAouU0MHD8EMsPpn1SpXrIQrM7xWU9vA9WvXcFH6qFIlnLgI\n" +
            "VT/6qHHDhvpro3ANBK///x8fF9+scROcx3lz5MIkCAGDEEeOi/BHjxyp99XXJYsVl1wUxyFnwDBD\n" +
            "3xjgbkAf/WnSyAdTCWShCCY7sF979zhclLjJOI3kVk77qS+7BaMRG6L2g/GWFAy83j17wU5VLFce\n" +
            "3sSqlSu9j3Ng8aJF2JBypcuYW5EjF6wqrCcvmNIQ7Asqr1W9BrYOOgi4jONQ4tJNBQGmCfD8lYHI\n" +
            "kROGpnvXrurQ9Opdq3r1SuXKz5w+w9FUff/dMj6ehP2DgdetS9ce3brXqVmrWOHCuMiHuR5dB3Do\n" +
            "fRo0qPJhZXUojZ7A38Em2B/ekyLjx36LncmDpULOXJ9Vrdq4QUPLsgj2Fbbxi5o1MZ6pCaFOrdrN\n" +
            "mzTBUL975w7mhugkj/XHH1bGnsfQRkFpCPC/ehVnZpXKlc22cuTCcYEmHB8cdDLGrs4pZwvmzvzS\n" +
            "KsPwoaaj4eA1oCWMbczwIY/dlHGbY2PXXwxQEDBJXgeG/IQNN875P71+x9vdEK5lC1XVgNVXpK0F\n" +
            "+9UjIuph0BRl+FW2AVZuJtxbF4ZgEpAkYhyMoHv3yAAkRQeAHPb4MU4vXA8hM8sSE5BxZd6+devs\n" +
            "mbMmT5w4bfIU31Wr4DLgSsVcxhTgjOBKjpOSy8jwBp9FRsIjEE3gxPETkkSMevQiQHxc3PNnz/i/\n" +
            "ZmJCAmQwZl5iImw/GAAjOSQkBDFkXI35sVnUFvn0Kc4kKqMSKNN3MBpXgKwD1gRXV1iip0+fMld0\n" +
            "LMKTsCe7du7EfE3thylT/Hx9YVh5WdOrtVQCQMYGqg2Jj2fHAEvfqKn2RmSk9J+Ieq78I8dq4UPt\n" +
            "2L59zix1aKZOngwjdcV1JxJyRVkXYA1x3JctXTpl0iSUWrxw0dEjR/UVViqjXXRPPy5wl8hQzVBX\n" +
            "kCLoNrfILJCYyK3mmoioIY6MjMRxNJUMIAkSOxPWARsr7WLbcVzoFkkNALYCOwrHnWrA82fPIyIi\n" +
            "9LIAdzJCRHgEzhYAjgOSODNhoVibg2kgQqMSYSDgzBf8xrzH4QMfvz1XMlZopUOMCcoY6YEPY4+6\n" +
            "Hs28GvAsQN1DGfsgRL+HMpl/dnABIiAkxvhPRC1PVBi6H02npb8OeWYuIHuC3nQWYe9tpoCaaNqL\n" +
            "2BlC5y06XopIll3HzhA6b9HxUkSy7Dp2hvBSBADpyHtBpkUcczMtZUemRRxzMy1lR6ZFHHMzLWVH\n" +
            "pkUcczMtpcOjaUAFmFZAOHk78teuPx3+s8vW59rLVHRIk5gyHDodull7oYPjkxfqFomYJHXfVEJK\n" +
            "XGJK5REHjCZ8YSBuGDdTP4zMWH3Ut8fLtiErU02SntQsRRxrAMAzy5MCYFfIVNkO8MzypADYFTJV\n" +
            "tgM8szwpAF6y3hY/YFXe8b6hd4ZH0wDEJqZwwj9srT9NA8LXU45zcYA6OnD9P3X5yeJ1AZbXQPF5\n" +
            "zbN8XvM2b6x88ehxvLkAEftqjLb6OGOX+kye+uSE0fR7vMd7/LfAm2kAQp4nJKemw73/ZPQhGb2j\n" +
            "Nly3GIbY+OR9p0LGLbzoenmk/0Kvb3ngAsT9YLUAselU8D+5vJK6U4+npaenpr9+FGldQjfMkdUe\n" +
            "kQQok9ThiSTMtDssWZQtpA5PPJBpKT1XGJ3U4YkHMi2l5wqjkzo88UCmWV4UHPG2+sD7hogfu6FM\n" +
            "TAMqCYpQi+1Pol9m78qHtX0/8Fmz8pj6jyc2IfnIpbBJK660HXXY7ZXT8y9OWWq8ctp4N9Sa7YGb\n" +
            "9gbt1N4NdcH/6dWbagHi4MWwf29r3hZdsM/O6AR1H9H9iHg4Jqp5FzLdGMs2v9Uu0JHFIu9Qsx1Z\n" +
            "qeSn1ZAjLDW/byjr+O9tKBPTAMBr4D+IVx9G/6vre9a/a7r248H7CrTfXLrz1irGhyoaDtzfatih\n" +
            "TmPUp+4GTjs9Ut4oucr5jZKnL4fvOhmcrctWVvgfHTYHPlE2KPR54kvj03V2/Hi7xoK3agjKhJl2\n" +
            "wc4ImGUU+pk09B4/P2RuGnBCvEhM5qfrD14PNx+vbOj3QSO/f2my9v+02lCwvfF5qx78vNV+9Xmr\n" +
            "scfM91DPPT9x8eUZyx3eQ712X9B/dtpCu/DHVhvOBao79iJfJMUYjgNgnJyZnPQWvEMRwHspT3iH\n" +
            "IoBeKos1/IM3lBX8eDVb8L6hd4a9IWfTYNd7HveKL3rfcyXMdS+zCr/yWfOnZuvko5ify0cxRx52\n" +
            "/3rFZf3rFVN9r//VNY/45xbrjwREoEUYhaex1rvfPe0a8J6yHEFlL6U88e8AVvUDVugJ/2gNWRSy\n" +
            "2DFd7X1DxD9CQ5l7DYJnL17xkr77chjGMxwHNbaN+J8ar/lL8/W52mwq0XHrR93Up7TrD9jXwvzm\n" +
            "1QkuQIxzffOq/ZRTv2tmrjv+j1Ybjt9STzG9eJnCm6w8wdM2ZLov3hae9t0P3pAFP+OGfmy8b+id\n" +
            "4aWhtzANABwH3tdw6nbkX9tv5vBW1qGh3y8b+f2+ydr/aLE+X7vNZTpv5ZcyGw7cxy9lcgGi37Qz\n" +
            "JXq6XhLT0O9/d9py6b56LiA6IZlviybQXUuPhdFjXQCUhnspQCcpSBIwMt2KCKPHuuAFuo53fbum\n" +
            "MN4LEvbinmDXFMZ7QcJenICsJx0hCrqmpZRjJRZ9Rx0doqBrWko5VmLRd9TRIQq6pqWUYyUWfUcd\n" +
            "HaKga1pKOVZi0XfU0SEKuqbIyjSoOjKrRfDsRVKE4fbfCXtRsE/Gy6AYPvAxFyAKcQHC/L72/pbD\n" +
            "D1Xqvev3Tdea74lu5Fu03+4HT9UtVZEvkmQewW7o/aEMMAlQtjCEmXavAbDLElMQGWAS0GXCkdFJ\n" +
            "RwWJBd6TgCOjk44KEgu8JwFHRiftCoCQFFQBd8ELdE1AGEdIlmhaBC/QNQFhHCFZomkRvEDXBIRx\n" +
            "hGSJpkXwAl0TEMYRkiWaFsELqPB2XgOBaQVfMBubkFx/2gndNDD82sfvX10LEKU6b8vTbvNv3V/i\n" +
            "0HTWqbiX6sEbOAu8q+o9fpbI9Cz8ofC+oXeGp4ayahos5V8mpwVFxKW/fp2W/nrW7ju/0xYmJfzG\n" +
            "Z81vGq/J+KqFEf7QYv38fXd5PyVq8PTAtd6coWsm7YIOPVeXKThCzzUKuZWSpHfoylkv+Lb6gL1I\n" +
            "Vgq+rT5gL5LFgu/xs4GDadBPAvsJIUx6OsZ2fEKSGtvXg2PKDd6nmwDHUHbw3puh6gE1WISg8DiY\n" +
            "FS/1qzz3nuhJHcJbBFXAQxFAsixq3pOekJUidl5P2nMdkZUidl5P2nMd4aWInhRZBMr2mAIhpMSE\n" +
            "UvKQFFkEyvaYAiGkxIRS8pAUWQTK9pgCIaTEhFLykBRZBMr2mAIhpMSEUvKQFFkEyvaYAvEuEwod\n" +
            "4TEvecvDq5S0Sdtuqn8ubBYBAfzk7TdfGd+tCo9J4rfw9X78ffD3b/E9BJ52/g9+UN439M7QK/yv\n" +
            "mgYALsD9iHi+WOFhZHzD6Sc+8IE5MN/+BrnBtBNccUxOVdOQrL+1yYJ32xGWUlmp5MduSLJ+ug15\n" +
            "qvDdGrLgfUPvjB+wobdYa/BeY1jUS/lc7fFbTysO2w+7gFnG4RsRLAZPwfudCz8JeN8JWURWKvlp\n" +
            "NZR1SHM/drvvG3pnsP4fwGsQwHF4FJkQm6hui0Ll1x5F8ympF4kp8BqSDLfCET/Spv7Ye/A93uNn\n" +
            "jB/SNBDR8ckPIxNgCDAyEUP+8f6efIfB/1ZFqIzYUsqSdERWdARUVs38XBp6j586lGnI9JDrCpAF\n" +
            "wtjj8JiX8CC4QklGIEnJkpgwMk1YGMdcwqS0IkIKQzAJmGkDFkZkCkamgp2UpJ2xJAmSgCR1wchR\n" +
            "sJOStDOWJEESkKQuGDkKdlKSdoaxLhBKyT1LYgqEkIwpEJLUY10glJJ7lsQUCCEZUyAkqce6QCgl\n" +
            "9yyJKRBCMqZASFKPdYFQSu5ZElMghGRMgZCkHusCoZTcsySmQAiZuddAPe8w6sxQYxIw0zYFgAxg\n" +
            "pjWQ1GMKhC4DzCWYFJ4CYGRakwBlPaZAMElIkoIe25NEVpKIHXkR9NieJLKSROzIi6DH9iRhSWYR\n" +
            "71bqHfC+oXeGY0PvPqHItN9Z2TAvOpYsJMlYeO/IirJFRzXjYihI8q2gl/JUw0+xIb2szthzdVhK\n" +
            "WZKm5A67jj22w1LKkjQld9h17LEdllKWpCm5w65jj+2wlLIkTckddh17bIdbqdev/x/gLQQnwRBL\n" +
            "tAAAAABJRU5ErkJggg==";

    static final String username = "faulknerkioskdirections@gmail.com";
    static final String password = "FaulkPassword";
    String recipient;

    String subject;
    String message;

    int numDirectionFloors;

    boolean includeImage;

    boolean isPhone;

    public static String encodeToString(BufferedImage image) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public SendEmail(String recipient, String subject, String message, boolean includeImage, int numDirectionFloors){
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.includeImage = includeImage;
        this.numDirectionFloors = numDirectionFloors;
        this.isPhone = false;
        sendEmail();
    }

    public SendEmail(String recipient, String subject, String message, boolean includeImage, int numDirectionFloors, boolean isPhone){
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.includeImage = includeImage;
        this.numDirectionFloors = numDirectionFloors;
        this.isPhone = isPhone;
        sendEmail();
    }

    public void sendEmail() {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        try {
            DataSource fds;
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            // -- Create a new message --
            Message msg = new MimeMessage(session);

            MimeMultipart multipart = new MimeMultipart("related");

            int width = 0;
            int height = 0;
            BufferedImage bimg;
            String encoding = "";

            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "";

            String imageDirectionsPortion = "";

            for (int i = 1; i <= numDirectionFloors; i++) {
                try {
                    bimg = ImageIO.read(new File("combined" + i + ".png"));
                    width = bimg.getWidth();
                    height = bimg.getHeight();
                    encoding = SendEmail.encodeToString(bimg);
                } catch (Exception e) {
                }
                String tempString = "";
                if (!isPhone) {
                    tempString += "<img src =\"cid:imageDirections" + i + "\" width = \"" + width + "\" height = \"" + height + "\" border = \"0\" />";
                }
                tempString += "<img src=\"data:image/jpg;base64," + encoding + "\" />" + htmlText;
                imageDirectionsPortion = imageDirectionsPortion + tempString;
            }



            try {
                bimg = ImageIO.read(new File("resources/scaled_falkner_banner.png"));
                width = bimg.getWidth();
                height = bimg.getHeight();
            } catch (Exception e) {
            }

            if (!isPhone) {
                htmlText = "<img src=\"cid:imageLogo\" width = \"" + width + "\" height = \"" + height + "\" border=\"0\" /> ";
            }
            htmlText = htmlText + this.message;

            if (includeImage) {
                htmlText += imageDirectionsPortion;
            }

            htmlText = "<img src=\"data:image/jpg;base64," + headerImg + "\" />" + htmlText;

            messageBodyPart.setContent(htmlText, "text/html");

            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            fds = new FileDataSource(
                   "resources/scaled_falkner_banner.png");




            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<imageLogo>");

            multipart.addBodyPart(messageBodyPart);

            if (includeImage) {
                for (int i = 1; i <= numDirectionFloors; i++) {
                    messageBodyPart = new MimeBodyPart();
                    fds = new FileDataSource(
                            "combined" + i + ".png");

                    messageBodyPart.setDataHandler(new DataHandler(fds));
                    messageBodyPart.setHeader("Content-ID", "<imageDirections" + i + ">");

                    multipart.addBodyPart(messageBodyPart);
                }
            }


            msg.setContent(multipart);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient, false));
            msg.setSubject(subject);
            //msg.setText(message);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("Message sent.");
        } catch (MessagingException e) {
            System.out.println("Message failed to send; cause:" + e);
        }
    }
}