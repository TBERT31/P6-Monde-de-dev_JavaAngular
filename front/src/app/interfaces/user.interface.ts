export interface User {
    id: number;
    username: string;
    email: string;
    password: string;
    comments: number[];
    articles: number[];
    topics_subscribed: number[];
    createdAt: Date;
    updatedAt?: Date;
  }
  